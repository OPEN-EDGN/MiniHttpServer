package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.request.BaseHttpRequest
import java.io.Closeable

class ResponseWrapper():Closeable {
    fun wrap(httpResponse: BaseHttpResponse, httpRequest: BaseHttpRequest) {
        TODO("Not yet implemented")
    }

    override fun close() {
    }

}