package tech.openedgn.net.server.web.response.wrapper

import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.BaseHttpResponse
import java.io.Closeable

/**
 * Response 填充
 */
interface IWrapper :Closeable{
    fun wrap( httpRequest: BaseHttpRequest,httpResponse: BaseHttpResponse):Boolean
}