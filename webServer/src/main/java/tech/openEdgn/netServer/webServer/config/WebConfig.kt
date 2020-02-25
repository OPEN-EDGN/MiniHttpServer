package tech.openEdgn.netServer.webServer.config

import tech.openEdgn.netServer.webServer.data.METHOD
import tech.openEdgn.netServer.webServer.io.RequestBodyLoader
import tech.openEdgn.netServer.webServer.utils.getWebLogger
import java.io.File
import java.nio.charset.Charset
import kotlin.reflect.KClass

class WebConfig(val serverPort: Int) {
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
    var charset: Charset = Charsets.UTF_8


//    val requestBodyLoader:HashMap<METHOD, KClass<out RequestBodyLoader>> = TODO()


    inner class SafeMode{

        /**
         * 单连接最大请求长度
         */
//        val maxBufferSize:Long = 50 * 1024 * 1024

    }
}