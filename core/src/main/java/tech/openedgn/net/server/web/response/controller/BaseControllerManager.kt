package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.ResponseWriter
import java.io.Closeable

abstract class BaseControllerManager : Closeable {
    abstract fun addController(any: Any,classLoader: ClassLoader): Boolean
    open fun addControllerClass(any: Class<*>,classLoader: ClassLoader): Boolean {
        return addController(any.newInstance(),classLoader)
    }

    abstract fun loadController(request: BaseHttpRequest):ResponseWriter?

}