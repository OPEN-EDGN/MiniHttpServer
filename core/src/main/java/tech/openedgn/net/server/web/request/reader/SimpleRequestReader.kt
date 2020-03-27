package tech.openedgn.net.server.web.request.reader

import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.BadRequestException
import tech.openedgn.net.server.web.HeaderFormatException
import tech.openedgn.net.server.web.MethodFormatException
import tech.openedgn.net.server.web.WebServerInternalException
import tech.openedgn.net.server.web.request.bodyLoader.BaseBodyLoader
import tech.openedgn.net.server.web.utils.BufferedInputStream
import tech.openedgn.net.server.web.utils.dataBlock.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.DecodeUtils
import tech.openedgn.net.server.web.utils.safeCloseIt
import java.io.IOException
import java.io.InputStream
import kotlin.reflect.KClass

class SimpleRequestReader(
    inputStream: InputStream,
    remoteAddress: NetworkInfo,
    webConfig: WebConfig
) : BaseRequestReader(remoteAddress, webConfig) {
    private val requestReader = BufferedInputStream(inputStream)

    enum class RequestPointer {
        BEGIN,
        READ_METHOD_END,
        READ_HEAD_END,
        END
    }

    // 當前解析的進度
    private var pointer =
        RequestPointer.BEGIN

    override fun loadMethod() {
        choosePointer(RequestPointer.BEGIN)
        val methodLine =
            requestReader.readLineEndWithCRLF() ?: throw IOException("未讀取到合適的信息 「NULL」.")
        if (methodLine.matches(Regex("^(GET|POST)\\s(/)(.*)\\s\\w{4}(/).+$")).not()) {
            throw MethodFormatException(methodLine)
        } // METHOD 不符合規則
        val methodSplit = methodLine.split(" ")
        if (methodSplit.size != METHOD_SPIT_SIZE) {
            throw MethodFormatException(methodLine)
        }
        httpVersion = methodSplit[2]
        // http 版本
        if (httpVersion.toUpperCase() != "HTTP/1.1") {
            throw MethodFormatException(methodLine)
            // fixed : 如果請求不符合
        }
        method = METHOD.valueOf(methodSplit[0])
        // 確定 METHOD
        val path = methodSplit[1].split(Regex("#"), 2)[0]
        // 此時的location 可能附帶了表單信息
        val pathSpit = path.split(Regex("\\?"), 2)
        location = DecodeUtils.urlDecode(pathSpit[0])
        if (pathSpit.size == 1) {
            if (method == METHOD.GET) {
                rawFormData = ByteArrayDataBlock(ByteArray(0))
            }
        } else if (method == METHOD.GET) {
            rawFormData = ByteArrayDataBlock(DecodeUtils.urlDecode(pathSpit[1]).toByteArray(Charsets.UTF_8))
            DecodeUtils.decodeFormData(pathSpit[1], forms, logger)
        } else {
            logger.debug("在[$method]下已自动排除url中的表单数据:[${pathSpit[1]}]")
        }
        // method 解析完畢
        pointer =
            RequestPointer.READ_METHOD_END
    }

    override fun loadHeader() {
        choosePointer(RequestPointer.READ_METHOD_END)
        while (true) {
            val line = requestReader.readLineEndWithCRLF() ?: throw IOException("读取Header過程中未讀取到合適的信息 「NULL」.")
            if (line.isEmpty()) {
                break
                // 代表Header已经全部读取完成,
                // 並且在此時已經讀取完成 HEADER 和 FORM 之间的 “\r\n”
            }
            val headerSpit = line.split(Regex(":"), 2)
            if (headerSpit.size != 2) {
                throw HeaderFormatException(line)
            }
            headers[headerSpit[0].trim()] = DecodeUtils.urlDecode(headerSpit[1].trim())
        }
        pointer =
            RequestPointer.READ_HEAD_END
    }

    override fun loadBody() {
        choosePointer(RequestPointer.READ_HEAD_END)
        if (method == METHOD.POST) {
           checkTempFolder()
            // 建立临时文件夹
            val outputStream = tempDataBlockConstructorFun("post")
            val bytes = ByteArray(WebServer.CACHE_SIZE)
            while (requestReader.available() > 0) {
                outputStream.write(bytes, 0, requestReader.read(bytes, 0, bytes.size))
            }
            rawFormData = outputStream.openDataReader()
            logger.debug("表单块信息：$rawFormData")
            headers["Content-Length"]?.let {
                if (rawFormData.size < it.trim().toLong()) {
                    throw BadRequestException("POST 表单的实际长度低于 HEADER 标明长度. [${rawFormData.size} < ${it.length}]")
                }
            }
            // 將 POST 數據保存到數據塊下
            val bodyLoaderImplClass: KClass<out BaseBodyLoader>? =
                BaseBodyLoader.searchRequestBodyLoader(
                    headers,
                    webConfig.internalConfig.requestBodyLoader,
                    logger
                )
            if (bodyLoaderImplClass != null) {
                val requestBodyLoader = BaseBodyLoader.createNewDataBodyLoader(bodyLoaderImplClass, logger)
                logger.debug("表单解析将由 ${requestBodyLoader::class.java.simpleName} 完成.")
                if (requestBodyLoader.load(location, headers, rawFormData, forms)) {
                    requestBodyLoader.safeCloseIt(logger)
                    logger.debug("表单处理完成.")
                } else {
                    requestBodyLoader.safeCloseIt(logger)
                    throw BadRequestException("表单解析出现问题.")
                }
            } else {
                logger.debug("未找到适合[${headers["Content-Type"]}]的解析方案.")
            }
        } else {
            logger.warn("此会话并非 POST 请求，此方法不应该被调用.")
            throw WebServerInternalException("此会话并非 POST 请求，此方法不应该被调用.")
        }
        pointer = RequestPointer.END
    }

    /**
     * 检查临时文件夹是否存在
     */
    private fun checkTempFolder() {
        if (webConfig.tempFolder.isDirectory.not()) {
            webConfig.tempFolder.mkdirs()
        }
    }

    private fun choosePointer(status: RequestPointer) {
        if (pointer != status) {
            throw WebServerInternalException("函數執行步驟出現錯誤.[${pointer.name}]")
        }
    }
    companion object {
        const val METHOD_SPIT_SIZE = 3
    }
}
