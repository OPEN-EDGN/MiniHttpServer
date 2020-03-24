package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.request.BaseHttpRequest

interface IResponseFiller {
    fun fill(request: BaseHttpRequest, response: BaseHttpResponse):Boolean
}