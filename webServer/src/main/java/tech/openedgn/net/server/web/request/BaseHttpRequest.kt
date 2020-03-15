package tech.openedgn.net.server.web.request

import tech.openedgn.net.server.web.bean.FormItem
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import java.nio.charset.Charset

/**
 * 注意，这仅为  IRequest 的包装类
 */
abstract class BaseHttpRequest(protected val request: IRequest) : IRequest {
    override val sessionId: String
        get() = request.sessionId
    override val remoteAddress: NetworkInfo
        get() = request.remoteAddress
    override val method: METHOD
        get() = request.method
    override val location: String
        get() = request.location
    override val httpVersion: String
        get() = request.httpVersion
    override val headers: Map<String, String>
        get() = request.headers
    override val forms: HashMap<String, FormItem>
        get() = request.forms
    override val rawFormData: IDataBlock
        get() = request.rawFormData

    /**
     * 得到请求的 Content-Type 对象
     */
    abstract val contentType: String

    /**
     * 请求的编码类型
     */
    abstract val charset:Charset

    /**
     * 打印请求数据
     */
    abstract fun printInfo()
}