package tech.openedgn.net.server.web.request

import tech.openedgn.net.server.web.bean.FormItem
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.utils.IDataBlock

/**
 *
 */
interface IRequest {
    /**
     * 会话 ID
     */
    val sessionId: String
    /**
     *  请求的客户端地址
     */
    val remoteAddress: NetworkInfo
    /**
     * HTTP 类型
     */
    val method: METHOD
    /**
     *  请求地址
     */
    val location: String
    /**
     * http 版本
     */
    val httpVersion: String
    /**
     * HTTP 头 HEADER 信息
     */
    val headers: Map<String, String>
    /**
     * 所有的表单信息
     */
    val forms: HashMap<String, FormItem>

    /**
     * 原始表單數據
     */
    val rawFormData: IDataBlock
}