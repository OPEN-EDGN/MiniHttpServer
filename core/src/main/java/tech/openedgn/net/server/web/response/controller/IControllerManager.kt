package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.ResponseWriter
import java.io.Closeable

interface IControllerManager : Closeable {
     fun addController(any: Any): Boolean

    fun loadController(request: BaseHttpRequest): ResponseWriter?
}