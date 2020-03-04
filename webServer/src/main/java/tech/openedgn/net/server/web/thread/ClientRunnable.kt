package tech.openedgn.net.server.web.thread
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.io.OldRequestReader
import tech.openedgn.net.server.web.utils.AutoCloseRunnable
import java.net.Socket

class ClientRunnable(
    private val client: Socket,
    private val networkInfo: NetworkInfo,
    private val webConfig: WebConfig
) : AutoCloseRunnable() {
    private val httpReader by lazy {
        OldRequestReader(
                client.getInputStream(),
                webConfig.charset,
                networkInfo.toString(),
                webConfig.tempFolder
        ).registerAutoClose()
    }

    init {
        client.registerAutoClose()
        logger.remoteAddress = networkInfo.toString()
    }

    override fun execute() {
        httpReader.loadHeader()
        httpReader.loadBody(webConfig.baseRequestBodyLoader)
    }
}
