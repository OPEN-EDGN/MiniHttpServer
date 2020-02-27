package tech.openedgn.net.server.web.error

import java.io.IOException

abstract class HttpException(override val message: String, override val cause: Throwable? = null) :
    IOException(message, cause)

open class HttpHeaderException(override val message: String) :
    HttpException(message)

class MethodFormatException(methodLine: String) :
    HttpHeaderException("未知请求类型：[$methodLine]")
class HeaderFormatException(headerLine: String) :
    HttpHeaderException("无法解析此HEADER 字段：[$headerLine]")
class BodyFormatException(message: String) : HttpHeaderException("无法解析此BODY内容：[$message]")
