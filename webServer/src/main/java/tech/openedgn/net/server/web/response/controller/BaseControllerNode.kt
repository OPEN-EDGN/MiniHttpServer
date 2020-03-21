package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.request.HttpRequest
import tech.openedgn.net.server.web.utils.IMatcher

abstract class BaseControllerNode(protected val config: WebConfig.InternalConfig) : IControllerNode {

    override val child: Map<IMatcher, IControllerNode> = HashMap()
    override  var depth: Int = 0
    override val isEmpty: Boolean
    get() = child.isEmpty()



}
