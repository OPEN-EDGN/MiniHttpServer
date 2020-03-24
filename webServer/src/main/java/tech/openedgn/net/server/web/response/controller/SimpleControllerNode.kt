package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.response.matcher.IMatcher

class SimpleControllerNode(private val config: WebConfig.InternalConfig) : IControllerNode {
    override var depth: Int = 0
    override val isEmpty: Boolean
        get() = controller == null
    private var controller: Controller? = null
    private val childNodes = HashMap<IMatcher, SimpleControllerNode>()

    override fun select(method: METHOD, acceptLocationSplit: Array<String>, headers: Map<String, String>): Controller? {
        TODO("Not yet implemented")
    }


    override fun insert(bindLocationSplit: Array<IMatcher>, controllerBean: Controller): Boolean {
        val childNode = childNodes.getOrElse(bindLocationSplit[depth], {
            val simpleControllerNode = SimpleControllerNode(config)
            simpleControllerNode.depth = depth + 1
            childNodes[bindLocationSplit[depth]] = simpleControllerNode
            simpleControllerNode
        })
        if (depth == (bindLocationSplit.size - 1)) {
            // 到达节点尾部
            childNode.controller = controllerBean
        } else {
            childNode.insert(bindLocationSplit, controllerBean)
        }
        return true
    }

    override fun delete(bindLocationSplit: Array<IMatcher>, recursive: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(bindLocationSplit: Array<IMatcher>, controllerBean: Controller): Boolean {
        TODO("Not yet implemented")
    }

    override fun close() {

    }
}