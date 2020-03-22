package tech.openedgn.net.server.web.response.responseLoader

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.BaseHttpResponse
import tech.openedgn.net.server.web.response.IResponse
import tech.openedgn.net.server.web.response.controller.IController

class ResponseControllerLoader(
    networkInfo: NetworkInfo,
    webConfig: WebConfig
) : BaseResponseControllerLoader(networkInfo, webConfig) {
    private val internalConfig = webConfig.InternalConfig()
    private lateinit var controller: IController
    override fun responseExists(request: BaseHttpRequest): Boolean {
        val iController = internalConfig.rootControllerNode.find(request)
        return if (iController != null) {
            controller = iController
            true
        } else {
            false
        }
    }

    override fun loadResponse(httpResponse: BaseHttpResponse) {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

}