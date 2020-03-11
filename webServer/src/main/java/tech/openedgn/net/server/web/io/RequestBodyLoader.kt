package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.error.BadRequestException
import tech.openedgn.net.server.web.error.WebServerInternalException
import tech.openedgn.net.server.web.utils.IDataBlock
import tech.openedgn.net.server.web.utils.DataBlockOutputStream
import tech.openedgn.net.server.web.utils.WebLogger
import java.io.Closeable
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

/**
 *  此方法用于读取POST表单,每次会自动创建新的实例
 */
abstract class BaseRequestBodyLoader(protected val logger: WebLogger) : Closeable {
    /**
     * 读取表单的数据方法
     *
     * 此方法用于读取POST的表单数据
     * @param location String 请求地址
     * @param header Map<String, String> 头部信息
     * @param dataBlock BaseDataReader 原始POST 数据
     * @param block HashMap<String, BaseDataReader> 存入的POST 表单位置
     * @param tempFileCreateFun Function1<[@kotlin.ParameterName] String, DataReaderOutputStream> 临时数据存储创建函数
     * @return Boolean
     */
    abstract fun load(
            location: String,
            header: Map<String, String>,
            dataBlock: IDataBlock,
            block: HashMap<String, IDataBlock>,
            tempFileCreateFun: (name: String) -> DataBlockOutputStream
    ): Boolean

    companion object {
        /**
         *  創建新的 POST 解析類
         */
        @Throws(WebServerInternalException::class)
        fun createNewDataBodyLoader(
                clazz: KClass<out BaseRequestBodyLoader>,
                logger: WebLogger
        ): BaseRequestBodyLoader {
            try {
                val webLogger = WebLogger(clazz.java)
                webLogger.remoteAddress = logger.remoteAddress
                return clazz.javaObjectType.getConstructor(WebLogger::class.java).newInstance(webLogger)
            } catch (e: NoSuchMethodException) {
                throw WebServerInternalException("构造函数存在问题！", e)
            } catch (e: InstantiationException) {
                throw WebServerInternalException("在创建對象時出现错误！", e)
            }
        }

        /**
         *  判定解析方案
         */
        fun searchRequestBodyLoader(
                headers: Map<String, String>,
                loader: Map<String, KClass<out BaseRequestBodyLoader>>,
                oldLogger: WebLogger
        ): KClass<out BaseRequestBodyLoader>? {
            val logger = WebLogger(BaseRequestBodyLoader::class.java)
            logger.remoteAddress = oldLogger.remoteAddress
            val contentType = headers["Content-Type"] ?: throw BadRequestException("请求为POST但未知请求类型（未发现Content-Type字段）.")
            logger.debug("Content-Type:$contentType")
            val keys = loader.keys
            // 解析POST 请求的表单
            logger.debugOnly {
                it.debug("当前所有Content-Type解析方案：$keys")
            }
            for (key in keys) {
                if (contentType.toLowerCase().contains(key)) {
                    return loader[key]
                }
            }
            return null
        }

        /**
         * 在  IDataBlock 下搜索 data 字段首次出现的位置
         *
         * (搜索算法没有任何优化！)
         *
         * @param start Long 搜索开始的位置
         * @return Long 第一次出现的位置，如果未发现则返回 -1
         */
        fun IDataBlock.searchIndex(data: ByteArray, start: Long = 0): Long {
            if (start >= size) {
                throw IndexOutOfBoundsException("start Index > size")
            }
            val dataSize = data.size
            if (size < dataSize) {
                return -1
            }
            val lastIndex = size - dataSize + 1 //如果到此处无法得到结果则返回 -1
            var index: Long = start
            var next = 0
            var equalsTrue = false
            while (true) {
                if (index > lastIndex) {
                    return -1
                }
                next = 0
                equalsTrue = false
                for ((ind, value) in (index until (index + dataSize)).withIndex()) {
                    if (get(value) == data[ind]) {
                        equalsTrue = true
                    } else {
                        equalsTrue = false
                        break
                    }
                    if (get(value) == data[0] && next == 0) {
                        next = ind
                    }
                }
                if (equalsTrue) {
                    return index
                }
                if (next != 0) {
                    index += next
                } else {
                    index++
                }
            }
        }
    }
}

/**
 *  读取 Content-Type 为  multipart/form-data 的数据
 *
 *  一个最简单的表单如下所示：
 *
 *  ```http
 *   Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
 *   \r\n
 *   ------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
 *   Content-Disposition: form-data; name="text_form"\r\n
 *   \r\n
 *   data\r\n
 *   ------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
 *   Content-Disposition: form-data; name="file_form"; filename="hello.txt"\r\n
 *   Content-Type: text/plain\r\n
 *   \r\n
 *   data\r\n
 *   ------WebKitFormBoundary7MA4YWxkTrZu0gW--
 *  ```
 *
 */
class FormDataBodyLoader(logger: WebLogger) : BaseRequestBodyLoader(logger) {

    override fun load(
            location: String,
            header: Map<String, String>,
            dataBlock: IDataBlock,
            block: HashMap<String, IDataBlock>,
            tempFileCreateFun: (name: String) -> DataBlockOutputStream
    ): Boolean {
        val boundarySpit = (header["Content-Type"]
                ?: throw BadRequestException("未发现Content-Type字段.")).split(Regex("boundary="), 2)
        if (boundarySpit.size != 2) {
            throw  BadRequestException("未发现Content-Type下boundary 字段 ( $boundarySpit ).")
        }
        val boundary = boundarySpit[1].trim()
        val spitBoundary = "--$boundary\r\n".toByteArray(Charsets.ISO_8859_1)
        val endBoundary = "\r\n--$boundary--".toByteArray(Charsets.ISO_8859_1)
        val indexList = LinkedList<Pair<Long, Long>>()
        var index = 0L
        while (true) {
            index = dataBlock.searchIndex(spitBoundary, index)
            if (index == -1L) {
                break
            }
            val second = index + spitBoundary.size
            indexList.add(Pair(index, second -1 ))
            index = second
        }
        indexList.add(Pair(dataBlock.searchIndex(endBoundary,indexList.last.second),dataBlock.size))

        return true
    }

    override fun close() {

    }

}


