package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock

/**
 *
 */
interface IResponse {
    /**
     * 响应ID
     */
    var responseCode: ResponseCode

    /**
     * 响应的头部信息
     */
    val responseHeader:MutableMap<String,String>

    /**
     * 响应数据
     */
    val responseData: IDataBlock
}