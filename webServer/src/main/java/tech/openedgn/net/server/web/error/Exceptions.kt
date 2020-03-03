package tech.openedgn.net.server.web.error

import java.io.IOException

abstract class HttpException(override val message: String, override val cause: Throwable? = null) :
    IOException(message, cause)

open class HttpHeaderException(override val message: String) :
    HttpException(message)

/**
 * 无法得知请求类型
 */
class MethodFormatException(methodLine: String) :
    HttpHeaderException("未知请求类型：[$methodLine]")

/**
 * 无法解析 HEADER 头部
 */
class HeaderFormatException(headerLine: String) :
    HttpHeaderException("无法解析此HEADER 字段：[$headerLine]")
/**
 * 错误的请求类型 ID = 400
 */
class BadRequestException(message: String) : HttpException(message)

/**
 * 服务器内部错误
 */
class WebServerInternalException(val mess: String, val throwable: Throwable? = null) : HttpException(mess, throwable)
