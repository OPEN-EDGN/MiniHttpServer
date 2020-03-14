package tech.openedgn.net.server.web

import tech.openedgn.net.server.web.request.bodyLoader.FormDataBodyLoader
import tech.openedgn.net.server.web.request.bodyLoader.BaseBodyLoader
import tech.openedgn.net.server.web.request.bodyLoader.FormUrlencodedBodyLoader
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.Closeable
import java.io.File
import java.nio.charset.Charset
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
     *  默认遍历
     */
    val indexFileName = mutableListOf("index.html", "index.htm", "index", "welcome-webServer.html")

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
     *  Web 服务器编码类型
     */
    @Volatile
    var charset: Charset = Charsets.UTF_8

    val requestBodyLoader: ConcurrentHashMap<String, KClass<out BaseBodyLoader>> =
            ConcurrentHashMap(mapOf(
                    Pair("multipart/form-data", FormDataBodyLoader::class),
                Pair("application/x-www-form-urlencoded", FormUrlencodedBodyLoader::class)
            ))

    inner class SafeMode {

        /**
         * 单连接最大请求长度
         */
//        val maxBufferSize:Long = 50 * 1024 * 1024
    }

    override fun close() {
    }
}
