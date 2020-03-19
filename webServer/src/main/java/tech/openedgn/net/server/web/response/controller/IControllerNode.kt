package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.request.HttpRequest
import java.io.Closeable

/**
 *
 *  树节点对象
 *
 *
 */
interface IControllerNode : Closeable {
    /**
     * 子节点索引表
     */
    val child: Map<IMatcher, IControllerNode>

    /**
     * 此树的深度
     */
    val depth: Int

    /**
     * 是否为空的节点
     */
    val isEmpty: Boolean

    /**
     * 树查找
     *
     * 根据给定的请求的路径 （`location` ）遍历树，
     *
     * TIPS: 此处可利用树的深度值来计算
     *
     * @param location List<String> 请求的地址切割
     * @param request HttpRequest 包含了 http 的头部信息
     * @param regexValues MutableList<String> 正则匹配数据容器
     * @param regexControllers MutableList<ControllerItem> 条件匹配容器
     */
    fun find(
        location: List<String>,
        request: HttpRequest,
        regexValues: MutableList<String>,
        regexControllers: MutableList<Controller>
    )

    /**
     *
     * 添加子树
     *
     * @param location List<String>
     * @param controllerBean Controller
     */
    fun add(location: List<String>, controllerBean: Controller): Boolean


}
