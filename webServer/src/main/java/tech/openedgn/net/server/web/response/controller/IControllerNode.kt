package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.request.HttpRequest
import tech.openedgn.net.server.web.utils.IMatcher
import java.io.Closeable

/**
 *
 *  树节点对象
 *
 *
 */
interface IControllerNode : Closeable {

    /**
     * 此树的深度
     */
    val depth: Int

    /**
     * 是否为空的节点
     */
    val isEmpty: Boolean

    /**
     * 根据 METHOD + HEADER 来查找解析方案
     *
     * @param request BaseHttpRequest HTTP请求信息，注意，此方法执行时还未解析POST表单信息
     * @return
     */
    fun find(
        request: BaseHttpRequest
    ): IController?

    /**
     * 添加子树
     *
     * @param method METHOD 请求类型
     * @param location String 绑定的地址
     * @param controllerBean Controller 适配器
     * @return Boolean 是否添加成功
     */
    fun add(method: METHOD, location: String, controllerBean: IController): Boolean


}
