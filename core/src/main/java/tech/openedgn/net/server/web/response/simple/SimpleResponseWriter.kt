package tech.openedgn.net.server.web.response.simple

import com.google.gson.Gson
import tech.openedgn.net.server.web.consts.ContentType
import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.*
import tech.openedgn.net.server.web.response.controller.ControllerData
import tech.openedgn.net.server.web.utils.dataBlock.AnotherFileDataBlock
import tech.openedgn.net.server.web.utils.dataBlock.ByteArrayDataBlock
import java.io.File
import java.lang.reflect.Method

class SimpleResponseWriter(private val data: Any, private val method: Method) : ResponseWriter {
    override fun fill(request: BaseHttpRequest, response: BaseHttpResponse): Boolean {
        when (val result = method.invoke(data, request)) {
            is File -> {
                AnotherFileDataBlock(result)
                response.contentType = ContentType.getFileContentType(result).application

            }
            is String -> {
                response.responseData = ByteArrayDataBlock(result.toByteArray())
                response.contentType = ContentType.TYPE_HTML.application

            }
            else -> {
                response.responseData = ByteArrayDataBlock(Gson().toJson(result).toByteArray())
                response.contentType = "application/json;charset=utf-8"

            }
        }
        response.responseCode = ResponseCode.HTTP_OK

        return true
    }

    override fun close() {
    }
}