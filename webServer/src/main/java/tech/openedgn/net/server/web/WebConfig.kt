package tech.openedgn.net.server.web

import tech.openedgn.net.server.web.request.bodyLoader.FormDataBodyLoader
import tech.openedgn.net.server.web.request.bodyLoader.BaseBodyLoader
import tech.openedgn.net.server.web.request.bodyLoader.FormUrlencodedBodyLoader
import tech.openedgn.net.server.web.response.controller.ILocationSplitRule
import tech.openedgn.net.server.web.response.controller.RegexLocationSplitRule
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.Closeable
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.LockSupport
import java.util.concurrent.locks.ReadWriteLock
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


    val requestBodyLoader: ConcurrentHashMap<String, KClass<out BaseBodyLoader>> =
        ConcurrentHashMap(
            mapOf(
                Pair("multipart/form-data", FormDataBodyLoader::class),
                Pair("application/x-www-form-urlencoded", FormUrlencodedBodyLoader::class)
            )
        )

    /**
     * 服务器内部定义数值，请勿在未知其用途的情况下修改！
     */
    inner class InternalConfig {
        @Volatile
        var locationRule : ILocationSplitRule =RegexLocationSplitRule()
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
