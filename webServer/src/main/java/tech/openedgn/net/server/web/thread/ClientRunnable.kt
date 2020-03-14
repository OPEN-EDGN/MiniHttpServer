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
        httpReader.loadHeader()
        if (httpReader.method == METHOD.POST) {
            httpReader.loadBody()
        }
    }
}
