package tech.openedgn.net.server.web.request.reader

import tech.openedgn.net.server.web.request.IRequest
import java.io.Closeable

/**
 *  解析 HTTP Request 的抽象方法
 */
interface IRequestReader :IRequest, Closeable {

    /**
     * 读取METHOD 和 location 的抽象方法
     */
    fun loadMethod()

    /**
     * 读取 HTTP 头部的抽象方法
     */
    fun loadHeader()

    /**
     * 读取 HTTP 表单的抽象方法
     */
    fun loadBody()
}
