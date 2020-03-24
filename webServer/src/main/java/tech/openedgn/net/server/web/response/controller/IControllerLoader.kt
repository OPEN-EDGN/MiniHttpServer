package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.BaseHttpResponse
import java.io.Closeable

/**
 * Controller 查找 & 解析抽象类
 *  生命周期为 单个会话
 */
interface IControllerLoader : Closeable {
    /**
     * 是否存在对应的解析器
     *
     *  根据请求信息判断是否存在对应的解析器
     *
     * @param request 请求信息
     * @return Boolean true or false
     */
    fun controllerExists(request: BaseHttpRequest): Boolean

    /**
     * 执行解析方法
     */
    fun executeController(request: BaseHttpRequest, httpResponse: BaseHttpResponse)


}