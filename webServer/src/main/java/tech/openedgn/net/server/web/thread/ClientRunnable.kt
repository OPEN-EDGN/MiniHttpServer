package tech.openedgn.net.server.web.thread

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.io.IRequestReader
import tech.openedgn.net.server.web.io.RequestReaderImpl
import tech.openedgn.net.server.web.utils.AutoCloseRunnable
import java.net.Socket

class ClientRunnable(
    private val client: Socket,
    private val networkInfo: NetworkInfo,
    private val webConfig: WebConfig
) : AutoCloseRunnable() {
    private val httpReader: IRequestReader by lazy {
        RequestReaderImpl(
            client.getInputStream(),
            networkInfo, webConfig
        )
    }

    init {
        client.registerAutoClose()
        logger.remoteAddress = networkInfo.toString()
    }

    override fun execute() {
        httpReader.registerAutoClose()
        httpReader.loadMethod()
        httpReader.loadHeader()
        if (httpReader.method == METHOD.POST) {
            httpReader.loadBody()
        }
    }
}
