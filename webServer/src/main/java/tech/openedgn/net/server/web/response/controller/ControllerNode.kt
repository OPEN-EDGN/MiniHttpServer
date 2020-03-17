package tech.openedgn.net.server.web.response.controller

/**
 *
 *  simple: /search/{name}/message
 *
 */
interface ControllerNode {

    val path:String
    /**
     * 绑定的名称
     */
    val name:String
    /**
     * 完整匹配
     */
    var child:Map<String,ControllerNode>

    /**
     * 正则匹配块
     */
    val regexChild:Map<Regex,ControllerNode>

    

}