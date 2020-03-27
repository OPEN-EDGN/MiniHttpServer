package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.ResponseWriter
import tech.openedgn.net.server.web.response.simple.SimpleResponseWriter

class ControllerManager : BaseControllerManager() {
    override fun addController(any: Any, classLoader: ClassLoader): Boolean {
        return true
    }

    override fun loadController(request: BaseHttpRequest): ResponseWriter? {
        return SimpleResponseWriter()
    }

    override fun close() {
    }

}