package tech.openedgn.net.server.web.response

import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.response.controller.IController

class ResponseErrorWriter(private val errorMap: Map<ResponseCode, IController>) {
    fun write(response: BaseHttpResponse, code: ResponseCode) {
            TODO("wait ...")
    }
}