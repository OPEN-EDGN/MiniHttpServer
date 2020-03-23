package tech.openedgn.net.server.web.thread

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.request.HttpRequest
import tech.openedgn.net.server.web.request.reader.IRequestReader
import tech.openedgn.net.server.web.request.reader.SimpleRequestReader
import tech.openedgn.net.server.web.response.BaseHttpResponse
import tech.openedgn.net.server.web.response.HttpResponse
import tech.openedgn.net.server.web.response.controller.BaseControllerLoader
import tech.openedgn.net.server.web.response.controller.SimpleControllerLoader
import tech.openedgn.net.server.web.response.writer.BaseHttpWriter
import tech.openedgn.net.server.web.response.writer.HttpWriter
import tech.openedgn.net.server.web.utils.AutoClosedRunnable
import tech.openedgn.net.server.web.utils.safeCloseIt
import java.lang.Exception
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
        SimpleRequestReader(
            client.getInputStream(),
            networkInfo, webConfig
        )
    }

    /**
     * 数据包装类
     */
    private val httpRequest: BaseHttpRequest by lazy {
        HttpRequest(httpReader)
    }

    /**
     * 响应的储存位置
     */
    private val httpResponse: BaseHttpResponse by lazy {
        HttpResponse(networkInfo)
    }

    /**
     * 响应回馈
     */
    private val httpWriter: BaseHttpWriter by lazy {
        HttpWriter(networkInfo.toString(), client.getOutputStream())
    }

    private val controllerLoader: BaseControllerLoader by lazy {
        SimpleControllerLoader(
            networkInfo,
            webConfig
        )
    }

    override fun execute() {
        httpReader.loadMethod()
        logger.info("收到${httpReader.method}請求,請求路徑：[${httpReader.location}].")
        val internalConfig = webConfig.InternalConfig()
        httpReader.loadHeader()
        try {
            if (controllerLoader.responseExists(httpRequest)) {
                if (httpReader.method == METHOD.POST) {
                    logger.debug("")
                    httpReader.loadBody()
                    //  对于 POST 请求，如果存在Controller则会继续读取POST 请求，否则进入响应阶段
                }
                controllerLoader.loadResponse(httpResponse)
            } else {
                internalConfig.simpleResponseErrorWriter.write(httpResponse, ResponseCode.HTTP_NOT_FOUND)
                // 未查询到解析方案，返回 404
            }
        } catch (e: Exception) {
            logger.debug("在处理请求的过程中发生错误！", e)
            internalConfig.simpleResponseErrorWriter.write(httpResponse, ResponseCode.HTTP_UNAVAILABLE)
            // 内部错误  503
        }
        if (httpResponse.isEmpty) {
            internalConfig.simpleResponseErrorWriter.write(httpResponse, ResponseCode.HTTP_BAD_GATEWAY)
            /**
             * 未获取到信息 502
             */
        }
        internalConfig.responseWrapper.wrap(httpResponse,httpRequest)
        // 填充扩展数据
        httpWriter.write(httpResponse)
        // 响应
        httpRequest.printInfo()

    }

    override fun close() {
        httpReader.safeCloseIt(logger)
        httpResponse.safeCloseIt(logger)
        httpWriter.safeCloseIt(logger)
        client.safeCloseIt()
        logger.debug("对象关闭完成.")
    }

}
