package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.utils.IDataBlock
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
     *  请求的客户端地址
     */
    val remoteAddress: NetworkInfo
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
    val forms: Map<String, IDataBlock>

    /**
     * 原始表單數據
     */
    val rawFormData: IDataBlock
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
