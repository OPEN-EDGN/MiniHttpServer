package tech.openedgn.net.server.web.response.simple

import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.response.BaseHttpResponse
import tech.openedgn.net.server.web.utils.dataBlock.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock

class EmptyResponseFill() {
    val map = HashMap<ResponseCode, IDataBlock>(
        mapOf(
            Pair(
                ResponseCode.HTTP_OK,
                ByteArrayDataBlock(javaClass.getResourceAsStream("/res/html/HelloWorld.html").readBytes())
            ),
            Pair(
                ResponseCode.HTTP_BAD_REQUEST,
                ByteArrayDataBlock(javaClass.getResourceAsStream("/res/html/400.html").readBytes())
            ), Pair(
                ResponseCode.HTTP_NOT_FOUND,
                ByteArrayDataBlock(javaClass.getResourceAsStream("/res/html/404.html").readBytes())
            ),
            Pair(
                ResponseCode.HTTP_UNAVAILABLE,
                ByteArrayDataBlock(javaClass.getResourceAsStream("/res/html/503.html").readBytes())
            )

        )
    )

    fun write(response: BaseHttpResponse, code: ResponseCode) {
        val item = map.getOrElse(code, {
            when (code.codeId) {
                in 400..499 ->
                    map[ResponseCode.HTTP_BAD_REQUEST]!!
                else ->
                    map[ResponseCode.HTTP_UNAVAILABLE]!!
            }
        })
        response.responseCode = code
        response.responseData = item
        response.contentType = "text/html; charset=utf-8"
    }
}