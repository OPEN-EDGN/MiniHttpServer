package tech.openedgn.net.server.web

import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.thread.ServerSocketRunnable
import tech.openedgn.net.server.web.utils.WebLoggerConfig
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.Closeable
import java.net.SocketException
import java.util.concurrent.ThreadFactory
import javax.net.ServerSocketFactory

/**
 *
 *   此 WebServer 并未完全实现 HTTP 1.1
 *
 *
 */
class WebServer @JvmOverloads constructor(
    private val serverPort: Int,
    private val factory: ServerSocketFactory = ServerSocketFactory.getDefault()
) : Closeable {

    private val logger = getWebLogger()

    /**
     * 软件配置
     */
    val webConfig = WebConfig(serverPort)

    private val serverSocket =
            factory.createServerSocket(webConfig.serverPort) ?: throw SocketException("端口[$serverPort] 绑定错误！")
    // HTTP 端口绑定套接字
    private val serverSocketRunnable by lazy {
        ServerSocketRunnable(
            serverSocket,
            webConfig
        )
    }
    init {
        HttpThreadFactory().newThread(serverSocketRunnable).start()
    }

    override fun close() {
        serverSocket.close()
        webConfig.close()
    }

    fun enable() {
        webConfig.accept = true
        webConfig.printInfo()
    }

    fun disable() {
        webConfig.accept = false
    }

    class HttpThreadFactory : ThreadFactory {
        override fun newThread(r: Runnable): Thread {
            val thread = Thread(r, "AcceptThread")
            thread.priority = Thread.MAX_PRIORITY
            thread.setUncaughtExceptionHandler { t, e ->
                getWebLogger(r)
                        .error("[ ${t.name} ] 发生未知错误!", e)
            }
            return thread
        }
    }

    companion object {
        @JvmStatic
        var debug: Boolean
            set(value) {
                WebLoggerConfig.debug = value
            }
            get() = WebLoggerConfig.debug

        @JvmStatic
        var loggerOutputHook
            get() = WebLoggerConfig.loggerOutputHook
            set(value) {
                WebLoggerConfig.loggerOutputHook = value
            }

        /**
         * 流的最大缓存大小
         */
        const val CACHE_SIZE = 1024
        /**
         * 最大内存的缓存大小
         */
        const val MEMORY_CACHE_SIZE = CACHE_SIZE * 4
    }
}
