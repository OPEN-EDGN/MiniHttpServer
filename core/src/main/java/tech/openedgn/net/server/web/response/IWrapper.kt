package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.request.BaseHttpRequest
import java.io.Closeable

/**
 * Response 填充
 */
interface IWrapper :Closeable{
    fun wrap(httpResponse: BaseHttpResponse, httpRequest: BaseHttpRequest):Boolean
}