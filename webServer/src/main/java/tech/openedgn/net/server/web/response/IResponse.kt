package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import java.io.Closeable

/**
 * 响应的抽象方法
 */
interface IResponse {
    /**
     * 响应ID
     */
    val responseCode: ResponseCode

    /**
     * 响应的头部信息
     */
    val responseHeader: Map<String, String>

    /**
     * 响应数据
     */
    val responseData: IDataBlock
}