package tech.openedgn.net.server.web.thread

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.request.HttpRequest
import tech.openedgn.net.server.web.request.reader.IRequestReader
import tech.openedgn.net.server.web.request.reader.RequestReaderImpl
import tech.openedgn.net.server.web.response.BaseHttpResponse
import tech.openedgn.net.server.web.response.ErrorResponse
import tech.openedgn.net.server.web.response.HttpResponse
import tech.openedgn.net.server.web.response.responseLoader.BaseResponseControllerLoader
import tech.openedgn.net.server.web.response.responseLoader.ResponseControllerLoader
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
        RequestReaderImpl(
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

    private val responseControllerLoader: BaseResponseControllerLoader by lazy {
        ResponseControllerLoader(networkInfo, webConfig)
    }

    override fun execute() {
        httpReader.loadMethod()
        logger.info("收到${httpReader.method}請求,請求路徑：[${httpReader.location}].")
        val internalConfig = webConfig.InternalConfig()
        httpReader.loadHeader()
        try {
            if (responseControllerLoader.responseExists(httpRequest)) {
                if (httpReader.method == METHOD.POST) {
                    logger.debug("")
                    httpReader.loadBody()
                    //  对于 POST 请求，如果存在Controller则会继续读取POST 请求，否则进入响应阶段
                }
                responseControllerLoader.loadResponse(httpResponse)
            }else{
                internalConfig.responseErrorWriter.write(httpResponse,ResponseCode.HTTP_NOT_FOUND)
            }
        }catch (e:Exception){
            logger.debug("在处理请求的过程中发生错误！",e)
            internalConfig.responseErrorWriter.write(httpResponse,ResponseCode.HTTP_UNAVAILABLE)
            //
        }
        if (httpResponse.isEmpty) {
            internalConfig.responseErrorWriter.write(httpResponse,ResponseCode.HTTP_BAD_GATEWAY)
        }
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
