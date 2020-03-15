package tech.openedgn.net.server.web.thread

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.request.HttpRequest
import tech.openedgn.net.server.web.request.reader.IRequestReader
import tech.openedgn.net.server.web.request.reader.RequestReaderImpl
import tech.openedgn.net.server.web.utils.AutoClosedRunnable
import java.net.Socket

/**
 * 请求解析线程
 *
 * @property client Socket 连接对象
 * @property networkInfo NetworkInfo 网络信息
 * @property webConfig WebConfig 服务器的配置
 */
class ClientRunnable(
    private val client: Socket,
    private val networkInfo: NetworkInfo,
    private val webConfig: WebConfig
) : AutoClosedRunnable(networkInfo.toString()) {
    private val httpReader: IRequestReader = RequestReaderImpl(
        client.getInputStream(),
        networkInfo, webConfig
    )
    private val  httpRequest: BaseHttpRequest = HttpRequest(httpReader)
    init {
        client.registerCloseable()
        logger.remoteAddress = networkInfo.toString()
    }

    override fun execute() {
        httpReader.registerCloseable()
        httpReader.loadMethod()
        logger.info("收到${httpReader.method}請求,請求路徑：[${httpReader.location}].")
        httpReader.loadHeader()
        if (httpReader.method == METHOD.POST) {
            httpReader.loadBody()
        }
        httpRequest.printInfo()

    }

    /**
     * 输出日志
     */

}
