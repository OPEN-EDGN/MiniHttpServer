package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.request.BaseHttpRequest
import java.io.Closeable

interface ResponseWriter :Closeable{
    fun fill(request: BaseHttpRequest, response: BaseHttpResponse):Boolean
}