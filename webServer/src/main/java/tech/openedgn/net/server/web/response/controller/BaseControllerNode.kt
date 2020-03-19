package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebConfig

abstract class BaseControllerNode(private val config: WebConfig.InternalConfig) : IControllerNode {
    override val child: Map<IMatcher, IControllerNode> = HashMap()
    override  var depth: Int = 0
    override fun add(location: List<String>, controllerBean: Controller): Boolean {
        location[depth]
        return true
    }
}
