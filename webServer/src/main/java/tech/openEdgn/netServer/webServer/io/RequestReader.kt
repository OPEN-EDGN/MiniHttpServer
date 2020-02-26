package tech.openEdgn.netServer.webServer.io

import tech.openEdgn.netServer.webServer.bean.BaseDataReader
import tech.openEdgn.netServer.webServer.data.METHOD
import tech.openEdgn.netServer.webServer.data.MethodData
import tech.openEdgn.netServer.webServer.error.HeaderFormatException
import tech.openEdgn.netServer.webServer.error.MethodFormatException
import tech.openEdgn.netServer.webServer.utils.DataReader
import tech.openEdgn.netServer.webServer.utils.WebLogger
import tech.openEdgn.netServer.webServer.utils.decodeFormData
import tech.openEdgn.tools4k.safeClose
import java.io.*
import java.lang.NullPointerException
import java.net.URLDecoder
import java.nio.charset.Charset

class RequestReader(
    inputStream: InputStream,
    private val charset: Charset = Charsets.ISO_8859_1,
    private val logger: WebLogger, private val tempFolder:File) : Closeable {

    private val reader = DataReader(inputStream)
    val methodData: MethodData
    val header = HashMap<String, String>()
    val formData = HashMap<String, BaseDataReader>()

    init {
        val methodLine = reader.readLine() ?: throw NullPointerException("METHOD 读取中断！")
        if (methodLine.matches(Regex("^(GET|POST)\\s(/)(.*)\\s\\w{4}(/).+$")).not()) {
            throw MethodFormatException(methodLine)
        }
        val methodSplit = methodLine.split(" ")
        if (methodSplit.size != 3) {
            throw MethodFormatException(methodLine)
        }
        val method = METHOD.valueOf(methodSplit[0])
        var location = methodSplit[1].let {
            it.substring(0, if (it.indexOf("#") != -1) it.indexOf("#") else it.length)
        }
        // 剔除锚点

        val urlData = location.let {
            val indexOf = it.indexOf("?")
            if (indexOf != -1) {
                location = it.substring(0, indexOf)
                it.substring(indexOf + 1)
            } else {
                ""
            }
        }
        //解析出所有url下键值对
        if (method == METHOD.GET && urlData.isNotEmpty()) {
            logger.decodeFormData(urlData,formData,charset)
            // 解析 GET 附加表单
        } else if (urlData.isNotEmpty()) {
            logger.debug("在[$method]下已自动排除url中带的表单数据:{$urlData}")
            // POST 不会处理 URL 的附加数据
        } else {
            logger.debug("URL 中未发现无表单键值对.")
        }
        methodData = MethodData(method, URLDecoder.decode(location, charset.name()), methodSplit[2].toLowerCase())
        // 读取method 结束
        while (true) {
            val line = reader.readLine() ?: throw NullPointerException("Header 读取中断！")
            if (line.isEmpty()) {
                break
                // 代表Header已经全部读取完成
            }
            val headerSpit = line.split(Regex(":"), 2)
            if (headerSpit.size != 2) {
                throw HeaderFormatException(line)
            }
            header[headerSpit[0]] =URLDecoder.decode( headerSpit[1].trim(),charset.name())

        }
//     开始读取 POST 表单数据
        if (method == METHOD.POST){
            if (tempFolder.isDirectory.not()) {
                tempFolder.mkdirs()
            }
        }
    }


    override fun close() {
        reader.safeClose()
        header.clear()
        for (value in formData.values) {
            value.safeClose()
        }
    }

    fun printInfo(logger: WebLogger) {
        logger.info("接收实例 [${methodData}]")
        logger.debugOnly {
            header.forEach { (t, u) ->
                logger.debug("HEADER(name=\"$t\",value=\"$u\").")
            }
            logger.debug("HEADER LEN:${header.size} .")
            formData.forEach { (t, u) ->
                logger.debug("BODY(name=\"$t\",data=\"$u\")")
            }
            logger.debug("FORM LEN:${formData.size} .")

        }
    }


}