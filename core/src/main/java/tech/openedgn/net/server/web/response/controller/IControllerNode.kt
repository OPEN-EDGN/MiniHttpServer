package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.matcher.IMatcher
import java.io.Closeable

/**
 *
 *  树节点对象
 *
 * 这并非主要的框架接口代码
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
     * 查找解析适配器
     * @param method METHOD METHOD类型
     * @param acceptLocationSplit Array<String> 字符串的切割
     * @param headers Map<String, String> 请求头
     * @return Controller? 响应适配器，如果不错在则返回 NULL
     */
    fun select(
        method: METHOD,
        acceptLocationSplit: Array<String>,
        headers: Map<String, String>
    ): Controller?


    /**
     * 添加子树
     * @param bindLocationSplit Array<IMatcher> 绑定的地址切割
     * @param controllerBean Controller 适配器
     * @return 是否绑定成功
     */
    fun insert(bindLocationSplit: Array<IMatcher>, controllerBean: Controller): Boolean


    /**
     * 删除此 Controller
     * @param bindLocationSplit Array<IMatcher> 绑定的地址切割
     * @param recursive Boolean 是否删除所有子项
     * @return Boolean 是否删除成功
     */

    fun delete(bindLocationSplit: Array<IMatcher>, recursive: Boolean): Boolean


    /**
     * 更新 Controller
     *
     * 如果不存在则创建
     *
     * @param bindLocationSplit Array<IMatcher> 绑定的地址切割
     * @param controllerBean Controller 适配器
     * @return 是否更新成功
     *
     */
    fun update(bindLocationSplit: Array<IMatcher>, controllerBean: Controller): Boolean
}
