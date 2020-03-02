package tech.openedgn.net.server.web.thread
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.io.RequestReader
import tech.openedgn.net.server.web.utils.AutoCloseRunnable
import java.net.Socket

class ClientRunnable(
    private val client: Socket,
    private val networkInfo: NetworkInfo,
    private val webConfig: WebConfig
) : AutoCloseRunnable() {
    private val httpReader by lazy {
        RequestReader(
                client.getInputStream(),
                webConfig.charset,
                networkInfo,
                webConfig.tempFolder
        ).registerAutoClose()
    }

    init {
        client.registerAutoClose()
        logger.remoteAddress = networkInfo.toString()
    }

    override fun execute() {
        httpReader.loadBody(webConfig.requestBodyLoader)
        httpReader.printInfo(logger)

    }
}
