package tech.openedgn.net.server.web.thread

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.utils.AutoCloseRunnable
import java.io.Closeable
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ServerSocketRunnable(
    private val serverSocket: ServerSocket,
    private val webConfig: WebConfig
) : AutoCloseRunnable("HOST") {
    private val threadPool by lazy {
        val threadPoolExecutor = ThreadPoolExecutor(0, Integer.MAX_VALUE,
                if (webConfig.timeout == 0) Long.MAX_VALUE else (4 * webConfig.timeout.toLong()),
                TimeUnit.MILLISECONDS,
                SynchronousQueue())
        Closeable { threadPoolExecutor.shutdownNow() }.registerCloseable() // 注册自动销毁事件
        threadPoolExecutor
        // 客户端容纳的线程池
    }

    override fun execute() {
        logger.info("Web服务器已经启动，端口为：${webConfig.serverPort}")
        while (serverSocket.isClosed.not()) {
            try {
                val client: Socket = serverSocket.accept()
                val remoteAddress = client.remoteSocketAddress as InetSocketAddress
                val remoteNetworkInfo = NetworkInfo(
                    remoteAddress.address.hostAddress,
                    remoteAddress.port,
                    remoteAddress.hostName
                )
                logger.remoteAddress = remoteNetworkInfo.toString()
                if (webConfig.accept.not()) {
                    logger.info("收到来自[$remoteNetworkInfo]的连接，但未开启服务器，连接已被丢弃.")
                    client.close()
                    continue
                }
                if (webConfig.timeout != 0) {
                    client.soTimeout = webConfig.timeout
                }
                threadPool.execute(
                    ClientRunnable(
                        client,
                        remoteNetworkInfo,
                        webConfig
                    )
                )
                // 推到新线程执行,
            } catch (e: Exception) {
                if ((e is SocketException).not() || e.message!!.contains("closed").not()) {
                    logger.error("在处理请求的过程中发生错误", e)
                }
                // 如果是因为端口监听关闭而抛出异常，则不会抛出
            }
        }
    }
}
