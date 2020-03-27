package tech.openedgn.net.server.web.response.wrapper

import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.BaseHttpResponse

class SimpleWrapper:IWrapper {
    override fun wrap(httpRequest: BaseHttpRequest, httpResponse: BaseHttpResponse): Boolean {
        val header = httpResponse.responseHeader
        header["Content-Length"] = httpResponse.responseData.size.toString()
        return true
    }

    override fun close() {

    }

}