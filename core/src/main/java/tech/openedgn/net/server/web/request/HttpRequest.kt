package tech.openedgn.net.server.web.request

import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.utils.getWebLogger
import java.nio.charset.Charset

/**
 * HTTP 请求包装类
 *
 */
class HttpRequest(request: IRequest) : BaseHttpRequest(request) {
    private val logger = getWebLogger(request.remoteAddress.toString())
    override val contentType by lazy {
        if (method!=METHOD.POST){
            return@lazy ""
        }
        val rawContentType = request.headers.getOrDefault("Content-Type","")
        if (rawContentType.contains(Regex("(boundary=|;)"))){
            return@lazy rawContentType.split(Regex(";"),2)[0]
        }else{
            return@lazy rawContentType
        }
    }
    override val charset by lazy { Charsets.ISO_8859_1 }

    override val formText: String by lazy {
        if (method!=METHOD.POST){
            return@lazy ""
        }
        val rawContentType = request.headers.getOrDefault("Content-Type","").toLowerCase()
        if (rawContentType.contains("charset=")) {
            return@lazy rawFormData.toString(Charset.forName(rawContentType.split(Regex(";"),2)[1]))
        }else{
            return@lazy rawFormData.text
        }
    }

    override fun printInfo() {
        logger.debugOnly {
            logger.debug("请求解析完毕.")
            headers.forEach { (t, u) ->
                logger.debug("Header=[$t];内容=[$u].")
            }
            logger.debug("原始表单数据=${rawFormData}")
            forms.forEach { (t, u) ->
                logger.debug("表单名称=[$t];内容=$u.")
            }
            logger.debug("Header共有 ${headers.size} 个，而表单共有 ${forms.size} 个")
        }
    }
}