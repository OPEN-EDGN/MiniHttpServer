package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.consts.METHOD

/**
 * 存在 严重的线程安全问题
 *
 *  simple: /search/{REGEX:[^LIST$]}/message
 *
 */
interface ControllerNode {
    /**
     *  得到此节点的controller 对象，如无则返回 NULL
     */
    val controller: Controller?

    /**
     * 子节点索引表
     */
    val regexChild: Map<Regex, ControllerNode>

    /**
     * 此树的深度
     */
    val depth: Int

    /**
     * 树查找
     *
     * 此处需要依赖树的深度值来
     *
     * @param location List<String> 请求的地址切割
     * @param
     * @param regexValue MutableList<String> 正则匹配数据容器
     * @return Controller? Controller对象，如果未发现则返回 NULL
     */
    fun find(location: List<String>, method: METHOD, regexValue: MutableList<String>): Controller?

    /**
     *
     * 添加子树
     *
     * @param location List<String>
     * @param controller Controller
     */
    fun add(location: List<String>, controller: Controller)

}
