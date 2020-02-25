package tech.openEdgn.netServer.webServer.thread

import tech.openEdgn.netServer.webServer.bean.NetworkInfo
import tech.openEdgn.netServer.webServer.config.WebConfig
import tech.openEdgn.netServer.webServer.io.RequestReader
import tech.openEdgn.netServer.webServer.utils.AutoCloseRunnable
import java.net.Socket

class ClientRunnable(private val client: Socket, private val networkInfo: NetworkInfo, private val webConfig: WebConfig) : AutoCloseRunnable() {
    private val httpReader by lazy { RequestReader(client.getInputStream(), webConfig.charset,logger,webConfig.tempFolder).registerAutoClose() }

    init {
        client.registerAutoClose()
        logger.remoteAddress = networkInfo.toString()
    }

    override fun execute() {
        httpReader.printInfo(logger)


    }

}
