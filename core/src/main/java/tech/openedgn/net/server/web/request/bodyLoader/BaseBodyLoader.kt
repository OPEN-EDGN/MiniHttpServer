package tech.openedgn.net.server.web.request.bodyLoader

import tech.openedgn.net.server.web.request.FormItem
import tech.openedgn.net.server.web.BadRequestException
import tech.openedgn.net.server.web.WebServerInternalException
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import tech.openedgn.net.server.web.utils.WebLogger
import java.io.Closeable
import kotlin.reflect.KClass

/**
 *  此方法用于读取POST表单,每次会自动创建新的实例
 */
abstract class BaseBodyLoader(protected val logger: WebLogger) :Closeable {
    /**
     * 读取表单的数据方法
     *
     * 此方法用于读取POST的表单数据
     * @param location String 请求地址
     * @param header Map<String, String> 头部信息
     * @param dataBlock BaseDataReader 原始POST 数据
     * @param forms HashMap<String, BaseDataReader> 存入的POST 表单位置
     * @return Boolean
     */
    abstract fun load(
        location: String,
        header: Map<String, String>,
        dataBlock: IDataBlock,
        forms: HashMap<String, FormItem>
    ): Boolean

    override fun close() {

    }

    companion object{
        /**
         *  創建新的 POST 解析類
         */
        @Throws(WebServerInternalException::class)
        fun createNewDataBodyLoader(
            clazz: KClass<out BaseBodyLoader>,
            logger: WebLogger
        ): BaseBodyLoader {
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
            loader: Map<String, KClass<out BaseBodyLoader>>,
            oldLogger: WebLogger
        ): KClass<out BaseBodyLoader>? {
            val logger = WebLogger(BaseBodyLoader::class.java)
            logger.remoteAddress = oldLogger.remoteAddress
            val contentType = headers["Content-Type"] ?: throw BadRequestException(
                "请求为POST但未知请求类型（未发现Content-Type字段）."
            )
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
    }
}