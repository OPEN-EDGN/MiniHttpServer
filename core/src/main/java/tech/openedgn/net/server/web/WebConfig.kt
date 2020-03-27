package tech.openedgn.net.server.web

import tech.openedgn.net.server.web.request.bodyLoader.FormDataBodyLoader
import tech.openedgn.net.server.web.request.bodyLoader.BaseBodyLoader
import tech.openedgn.net.server.web.request.bodyLoader.FormUrlencodedBodyLoader
import tech.openedgn.net.server.web.response.wrapper.IWrapper
import tech.openedgn.net.server.web.response.controller.BaseControllerManager
import tech.openedgn.net.server.web.response.controller.ControllerManager
import tech.openedgn.net.server.web.response.simple.EmptyResponseFill
import tech.openedgn.net.server.web.response.wrapper.SimpleWrapper
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.Closeable
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class WebConfig(val serverPort: Int) : Closeable {
    fun printInfo() {
        logger.debugOnly {
            it.debug("临时目录:${tempFolder.absolutePath}.")
            it.debug("索引目录:${indexFolder.absolutePath}.")
        }
    }

    private val logger = getWebLogger()

    /**
     *临时文件位置
     */
    @Volatile
    var tempFolder: File = File(System.getProperty("java.io.tmpdir"), "WebServerTemp")

    /**
     * HTTP 目录索引
     */
    @Volatile
    var indexFolder: File = File(System.getProperty("java.io.tmpdir"), "WebServerWork")

    /**
     * 客户端连接最长阻塞时间
     */
    @Volatile
    var timeout: Int = 3000

    /**
     * 是否开始处理连接请求
     */
    var accept: Boolean = false

    /**
     * 添加 Controller 对象
     * @param controllerClass Class<*> Controller 的 Class 对象 (无构造函数)
     * @return Boolean 是否添加成功
     */
    fun addControllerClass(controllerClass: Class<*>) =
        internalConfig.controllerManager.addControllerClass(controllerClass,javaClass.classLoader)

    /**
     * 添加 Controller 对象
     * @param any Any Controller
     * @return Boolean 是否添加成功
     */
    fun addControllerClass(any: Any) = internalConfig.controllerManager.addController(any,javaClass.classLoader)

    val internalConfig = InternalConfig()
    /**
     * 服务器内部定义数值，请勿在未知其用途的情况下修改！
     */
    class InternalConfig {

        @Volatile
        var responseWrapper: IWrapper = SimpleWrapper()

        /**
         * post 解析方案
         */
        val requestBodyLoader: ConcurrentHashMap<String, KClass<out BaseBodyLoader>> =
            ConcurrentHashMap(
                mapOf(
                    Pair("multipart/form-data", FormDataBodyLoader::class),
                    Pair("application/x-www-form-urlencoded", FormUrlencodedBodyLoader::class)
                )
            )

        val emptyResponseWrapper = EmptyResponseFill()

        val controllerManager: BaseControllerManager = ControllerManager()

    }

    inner class SafeMode {

        /**
         * 单连接最大请求长度
         */
//        val maxBufferSize:Long = 50 * 1024 * 1024
    }

    override fun close() {
    }
}
