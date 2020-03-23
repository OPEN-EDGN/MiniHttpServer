package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.request.BaseHttpRequest
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
    fun select(
        request: BaseHttpRequest
    ): IController?

    /**
     * 添加子树
     *
     * @param location String 绑定的地址
     * @param controllerBean Controller 适配器
     * @return Boolean 是否添加成功
     */
    fun insert( location: String, controllerBean: IController)


    /**
     * 删除此 Controller
     * @param location String Controller 绑定的路径
     * @param recursive Boolean 是否删除所有子项
     * @return Boolean 是否删除成功
     */
    fun delete( location: String,recursive:Boolean):Boolean


    /**
     * 更新 Controller
     *
     * 如果不存在则创建
     *
     * @param location String 绑定的地址
     * @param controllerBean Controller 适配器
     * @return 是否更新成功
     *
     */
    fun update( location: String, controllerBean: IController):Boolean
}
