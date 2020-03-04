package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.utils.BaseDataReader
import java.io.Closeable

/**
 *  解析 HTTP Request 的抽象方法
 */
interface IRequestReader : Closeable {
    /**
     * 会话 ID
     */
    val sessionId: String
    /**
     * HTTP 类型
     */
    val method: METHOD
    /**
     *  请求地址
     */
    val location: String
    /**
     * http 版本
     */
    val httpVersion: String
    /**
     * HTTP 头 HEADER 信息
     */
    val headers: Map<String, String>
    /**
     * 所有的表单信息
     */
    val forms: Map<String, BaseDataReader>

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
