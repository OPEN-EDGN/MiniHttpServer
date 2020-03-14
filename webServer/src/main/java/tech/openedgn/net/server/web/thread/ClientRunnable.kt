package tech.openedgn.net.server.web.thread

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.request.reader.IRequestReader
import tech.openedgn.net.server.web.request.reader.RequestReaderImpl
import tech.openedgn.net.server.web.utils.AutoCloseRunnable
import java.net.Socket

class ClientRunnable(
    private val client: Socket,
    private val networkInfo: NetworkInfo,
    private val webConfig: WebConfig
) : AutoCloseRunnable(networkInfo.toString()) {
    private val httpReader: IRequestReader by lazy {
        RequestReaderImpl(
            client.getInputStream(),
            networkInfo, webConfig
        )
    }

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
