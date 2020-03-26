package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.BaseHttpResponse

class SimpleControllerLoader(
    networkInfo: NetworkInfo,
    webConfig: WebConfig
) : IControllerLoader {
    private val internalConfig = webConfig.InternalConfig()
    private lateinit var controller: Controller
    override fun controllerExists(request: BaseHttpRequest): Boolean {
        val iController = internalConfig.rootControllerNode.select(
            request.method,
            internalConfig.locationRule.acceptLocationSplit(request.location),
            request.headers
        )
        return if (iController != null) {
            controller = iController
            true
        } else {
            false
        }
    }

    override fun executeController(request: BaseHttpRequest, httpResponse: BaseHttpResponse) {
        val annotatedType = controller.field.annotatedType

    }

    override fun close() {
        TODO("Not yet implemented")
    }

}