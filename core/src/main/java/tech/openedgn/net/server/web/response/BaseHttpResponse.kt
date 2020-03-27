package tech.openedgn.net.server.web.response

import  tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.Closeable

abstract class BaseHttpResponse(protected val remoteInfo: NetworkInfo) : IResponse, Closeable {
    protected val logger = getWebLogger(remoteInfo.toString())

    /**
     * 是否为空白容器
     */
    abstract val isEmpty: Boolean

    /**
     * 扩展属性
     */
    val extraValues = HashMap<String, String>()

    abstract override var responseCode: ResponseCode
    final override val responseHeader: HashMap<String, String> = HashMap()
    abstract override var responseData: IDataBlock
    override fun close() {
        extraValues.clear()
        responseHeader.clear()
        responseData.close()
    }

    abstract var contentType: String
}