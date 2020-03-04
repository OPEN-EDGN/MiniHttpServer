package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import java.io.InputStream

class RequestReaderImpl(
    inputStream: InputStream,
    remoteAddress: NetworkInfo,
    webConfig: WebConfig
) : BaseRequestReader(remoteAddress,webConfig) {
    override fun loadMethod() {
    }

    override fun loadHeader() {
    }

    override fun loadBody() {
    }
}
