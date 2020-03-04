package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.error.WebServerInternalException
import tech.openedgn.net.server.web.utils.BaseDataReader
import tech.openedgn.net.server.web.utils.DataReaderOutputStream
import tech.openedgn.net.server.web.utils.WebLogger
import java.io.Closeable
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
     * @param dataReader BaseDataReader 原始POST 数据
     * @param reader HashMap<String, BaseDataReader> 存入的POST 表单位置
     * @param tempFileCreateFun Function1<[@kotlin.ParameterName] String, DataReaderOutputStream> 临时数据存储创建函数
     * @return Boolean
     */
    abstract fun load(
        location: String,
        header: Map<String, String>,
        dataReader: BaseDataReader,
        reader: HashMap<String, BaseDataReader>,
        tempFileCreateFun: (name: String) -> DataReaderOutputStream
    ): Boolean

    companion object {
        /**
         *  創建新的 POST 解析類
         */
        @Throws(WebServerInternalException::class)
        fun createNewDataBodyLoader(
            clazz: KClass<out BaseRequestBodyLoader>,
            loggerTag: String
        ): BaseRequestBodyLoader {
            try {
                val webLogger = WebLogger(clazz.java)
                webLogger.remoteAddress = loggerTag
                return clazz.javaObjectType.getConstructor(WebLogger::class.java).newInstance(webLogger)
            } catch (e: NoSuchMethodException) {
                throw WebServerInternalException("构造函数存在问题！", e)
            } catch (e: InstantiationException) {
                throw WebServerInternalException("在创建對象時出现错误！", e)
            }
        }
    }
}

class FormDataBodyLoader(logger: WebLogger) : BaseRequestBodyLoader(logger) {

    override fun load(
        location: String,
        header: Map<String, String>,
        dataReader: BaseDataReader,
        reader: HashMap<String, BaseDataReader>,
        tempFileCreateFun: (name: String) -> DataReaderOutputStream
    ): Boolean {
        logger.info("OK")
        return true
    }

    override fun close() {
    }
}
