package tech.openedgn.net.server.web.request

import java.nio.charset.Charset

/**
 * HTTP 请求包装类
 *
 */
class HttpRequest(request: IRequest) : BaseHttpRequest(request) {

    override val contentType by lazy {
        val rawContentType = request.headers.getOrDefault("Content-Type","")
        if (rawContentType.contains(Regex("(boundary=|;)"))){
            return@lazy rawContentType.split(Regex(";"),2)[0]
        }else{
            return@lazy rawContentType
        }
    }
    override val charset by lazy { Charsets.ISO_8859_1 }
}