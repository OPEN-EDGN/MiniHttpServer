package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock

class HttpResponse(remoteInfo: NetworkInfo) : BaseHttpResponse(remoteInfo) {
    private var pIDataBlock: IDataBlock? = null
    override var responseData: IDataBlock
        get() {
            return pIDataBlock!!
        }
        set(value) {
            pIDataBlock = value
        }

    override val isEmpty: Boolean
        get() = pIDataBlock == null

    override lateinit var responseCode: ResponseCode
}