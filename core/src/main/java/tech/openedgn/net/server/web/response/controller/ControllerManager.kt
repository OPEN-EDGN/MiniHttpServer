package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebServerInternalException
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.Controller
import tech.openedgn.net.server.web.response.Get
import tech.openedgn.net.server.web.response.Post
import tech.openedgn.net.server.web.response.ResponseWriter
import tech.openedgn.net.server.web.response.simple.SimpleResponseWriter
import tech.openedgn.net.server.web.utils.getWebLogger
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.log

class ControllerManager : BaseControllerManager() {
    private val logger = getWebLogger()
    private val controller = HashMap<String, Any>()

    private val mGetControllerPath = HashMap<String, ControllerData>()
    private val mPostControllerPath = HashMap<String, ControllerData>()

    @Synchronized
    override fun addController(any: Any, classLoader: ClassLoader): Boolean {
        val clazz = any.javaClass
        controller[clazz.name]?.let {
            if (it == any) {
                return false
            }
        }
        controller[clazz.name] = any
        loadAllMethod(clazz)
        return true
    }

    private fun loadAllMethod(clazz: Class<Any>): Boolean {
        val className = clazz.name
        if (clazz.isAnnotationPresent(Controller::class.java).not()) {
            logger.error("对象 $className 不是 Controller 对象！")
            return false
        }
        loop@ for (method in clazz.methods) {
            val mGetMethod = method.getAnnotation(Get::class.java)
            val mPostMethod = method.getAnnotation(Post::class.java)
            if ((mGetMethod != null || mPostMethod != null) && method.genericReturnType == Void.TYPE) {
                logger.debug("[$method] 无返回方法! ")
                continue@loop
            } else if (mGetMethod == null && mPostMethod == null) {
                continue@loop
            } else if (mGetMethod != null) {
                val bindLocation = mGetMethod.bindLocation.toLowerCase()
                mGetControllerPath[bindLocation] = ControllerData(method = method, className = className)
                logger.debug("添加一个 GET 请求：[${bindLocation}],来源：${method.toGenericString()} ")
            } else if (mPostMethod != null) {
                val bindLocation = mPostMethod.bindLocation.toLowerCase()
                mPostControllerPath[bindLocation] = ControllerData(method = method, className = className)
                logger.debug("添加一个 POST 请求：[${bindLocation}],来源：${method.toGenericString()} ")
            }
        }
        return true
    }

    override fun loadController(request: BaseHttpRequest): ResponseWriter? {
        val map = if (request.method == METHOD.GET) {
            mGetControllerPath
        } else {
            mPostControllerPath
        }
        map[request.location.toLowerCase()]?.let {
            return SimpleResponseWriter(controller[it.className]!!, it.method)
        }
        return null
    }

    override fun close() {
        controller.clear()
        mGetControllerPath.clear()
        mPostControllerPath.clear()
    }

}