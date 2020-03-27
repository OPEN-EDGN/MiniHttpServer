package tech.openedgn.net.server.web.response.simple

import tech.openedgn.net.server.web.consts.ResponseCode
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.BaseHttpResponse
import tech.openedgn.net.server.web.response.ResponseWriter
import tech.openedgn.net.server.web.utils.dataBlock.DataBlockOutputStream
import java.io.OutputStream
import java.io.PrintStream

class SimpleResponseWriter : ResponseWriter{
    override fun fill(request: BaseHttpRequest, response: BaseHttpResponse): Boolean {
        response.responseCode = ResponseCode.HTTP_OK
        val outputStream = DataBlockOutputStream()
        val writer = PrintStream(outputStream)
        writer.println("SHHSFSFHASD\n")
        writer.println("SHHSFSFHASD\n")
        writer.println("SHHSFSFHASD\n")
        writer.println("SHHSFSFHASD\n")
        response.contentType = "text/html;charset=utf-8"
        response.responseData = outputStream.openDataReader()
        return true
    }

    override fun close() {
    }

}