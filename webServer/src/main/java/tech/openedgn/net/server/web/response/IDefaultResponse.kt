package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.request.BaseHttpRequest

interface IResponseFiller {
    fun fillResponse(request: BaseHttpRequest,response: BaseHttpResponse)
}