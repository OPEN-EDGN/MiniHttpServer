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

        printInfo()
    }

    /**
     * 输出日志
     */
    private fun printInfo() {
        logger.debugOnly {
            logger.debug("请求解析完毕.")
            httpReader.headers.forEach { (t, u) ->
                logger.debug("Header=[$t];内容=[$u].")
            }
            logger.debug("原始表单数据=${httpReader.rawFormData}")
            httpReader.forms.forEach { (t, u) ->
                logger.debug("表单名称=[$t];内容=$u.")
            }
            logger.debug("Header共有 ${httpReader.headers.size} 个，而表单共有 ${httpReader.forms.size} 个")
        }
    }
}
