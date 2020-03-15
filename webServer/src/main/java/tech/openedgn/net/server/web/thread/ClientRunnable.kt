package tech.openedgn.net.server.web.thread

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.request.HttpRequest
import tech.openedgn.net.server.web.request.reader.IRequestReader
import tech.openedgn.net.server.web.request.reader.RequestReaderImpl
import tech.openedgn.net.server.web.response.HttpResponse
import tech.openedgn.net.server.web.response.IResponse
import tech.openedgn.net.server.web.response.writer.BaseHttpWriter
import tech.openedgn.net.server.web.response.writer.HttpWriter
import tech.openedgn.net.server.web.utils.AutoClosedRunnable
import tech.openedgn.net.server.web.utils.safeCloseIt
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
    private val httpReader: IRequestReader by lazy {
        RequestReaderImpl(
            client.getInputStream(),
            networkInfo, webConfig
        )
    }
    /**
     * 数据包装类
     */
    private val  httpRequest: BaseHttpRequest  by lazy {
        HttpRequest(httpReader)
    }
    /**
     * 响应的储存位置
     */
    private val httpResponse:IResponse by lazy {
        HttpResponse(networkInfo)
    }
    /**
     * 响应回馈
     */
    private val httpWriter:BaseHttpWriter by lazy {
        HttpWriter(networkInfo.toString(),client.getOutputStream())
    }


    override fun execute() {
        httpReader.loadMethod()
        logger.info("收到${httpReader.method}請求,請求路徑：[${httpReader.location}].")
        httpReader.loadHeader()
        if (httpReader.method == METHOD.POST) {
            httpReader.loadBody()
        }
        httpRequest.printInfo()
    }

    override fun close() {
        httpReader.safeCloseIt(logger)
//        httpResponse.safeCloseIt(logger)
        httpWriter.safeCloseIt(logger)
        client.safeCloseIt()
        logger.debug("对象关闭完成.")
    }

}
