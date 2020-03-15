package tech.openedgn.net.server.web.response.writer

import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.response.IResponse
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.*

class HttpWriter(private val tag: String, output: OutputStream) : BaseHttpWriter(output) {
    private val logger = getWebLogger(tag)
    private val printWriter = OutputStreamWriter(output, Charsets.ISO_8859_1)
    override fun write(response: IResponse) {
        printWriter.println("HTTP/1.1 ${response.responseCode.toPrintLine()}")
        logger.debug("响应状态码 [${response.responseCode.toPrintLine()}].")
        //响应状态码
        response.responseHeader.forEach { (t, u) ->
            printWriter.println("$t: $u")
        }
        printWriter.println()
        printWriter.flush()
        logger.debug("响应HEADER传递结束.")
        //响应HEADER
        response.responseData.openInputStream().copyTo(output,WebServer.CACHE_SIZE)
        // 响应主体
        logger.debug("响应数据传递结束.")
    }

    override fun close() {
        printWriter.close()
        output.close()
    }
    private fun OutputStreamWriter.println(line:String = ""){
        this.write(line)
        this.write("\r\n")
    }

    private fun OutputStreamWriter.print(line:String){
        this.write(line)
    }

}