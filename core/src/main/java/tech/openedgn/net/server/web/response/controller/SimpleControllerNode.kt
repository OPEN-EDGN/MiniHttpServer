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
        for (key in childNodes.keys) {
            if (key.matches(acceptLocationSplit[depth])) {
                return if (depth == (acceptLocationSplit.size - 1)) {
                    val controller = childNodes[key]?.controller
                    if (controller != null && controller.replyMethod == method) {
                        controller
                    } else {
                        null
                    }
                } else {
                    childNodes[key]?.select(method, acceptLocationSplit, headers)
                }
            }
        }
        return null
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
        if (childNodes.containsKey(bindLocationSplit[depth])) {
            if (depth < (bindLocationSplit.size - 1)) {
                // 到达节点尾部
                if (recursive){
                    for (value in childNodes.values) {
                        value.delete(bindLocationSplit,recursive)
                    }
                    childNodes.clear()
                }else{
                    childNodes[bindLocationSplit[depth]]?.controller = null
                }
            }
        }
        return true
    }

    override fun update(bindLocationSplit: Array<IMatcher>, controllerBean: Controller): Boolean {
        return insert(bindLocationSplit, controllerBean)
    }

    override fun close() {
        for (value in childNodes.values) {
            value.close()
        }
        childNodes.clear()
    }
}