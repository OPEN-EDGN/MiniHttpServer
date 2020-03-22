package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.OutputStream

abstract class BaseHttpResponse(private val remoteInfo: NetworkInfo) : IResponse {
    abstract val isEmpty:Boolean
    protected val logger = getWebLogger(remoteInfo.toString())
    override lateinit var responseCode: ResponseCode
    override val responseHeader: HashMap<String, String> = HashMap()
    override lateinit var responseData: IDataBlock
    final override fun close() {
        responseHeader.clear()
        responseData.close()
    }

}