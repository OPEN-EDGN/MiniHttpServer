package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.error.BadRequestException
import tech.openedgn.net.server.web.error.HeaderFormatException
import tech.openedgn.net.server.web.error.MethodFormatException
import tech.openedgn.net.server.web.error.WebServerInternalException
import tech.openedgn.net.server.web.utils.BufferedInputStream
import tech.openedgn.net.server.web.utils.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.DecodeUtils
import java.io.IOException
import java.io.InputStream
import kotlin.reflect.KClass

class RequestReaderImpl(
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
    private var pointer = RequestPointer.BEGIN

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
        if (pathSpit.size == 1) {
            location = pathSpit[0]
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
        logger.info("收到${method}請求,請求路徑：$location")
        pointer = RequestPointer.READ_METHOD_END
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
            headers[headerSpit[0]] = DecodeUtils.urlDecode(headerSpit[1])
        }
        pointer = RequestPointer.READ_HEAD_END
    }

    override fun loadBody() {
        choosePointer(RequestPointer.READ_HEAD_END)
        if (method == METHOD.POST) {
           checkTempFolder()
            // 建立临时文件夹
            val outputStream = tempBlockCreateFunc("post")
            val bytes = ByteArray(WebServer.CACHE_SIZE)
            while (requestReader.available() > 0) {
                outputStream.write(bytes, 0, requestReader.read(bytes, 0, bytes.size))
            }
            rawFormData = outputStream.openDataReader()
            headers["Content-Length"]?.let {
                if (rawFormData.size < it.toLong()) {
                    throw BadRequestException("POST 表单的实际长度低于 HEADER 标明长度. [${rawFormData.size} < ${it.length}]")
                }
            }
            // 將 POST 數據保存到數據塊下
            val bodyLoaderImplClass: KClass<out BaseRequestBodyLoader>? =
                BaseRequestBodyLoader.searchRequestBodyLoader(
                    headers,
                    webConfig.requestBodyLoader,
                    logger
                )
            if (bodyLoaderImplClass != null) {
                val requestBodyLoader = BaseRequestBodyLoader.createNewDataBodyLoader(bodyLoaderImplClass, logger)
                requestBodyLoader.registerCloseable()
                logger.debug("表单解析将由 ${requestBodyLoader::class.java.simpleName} 完成.")

                if (requestBodyLoader.load(location, headers, rawFormData, forms, tempBlockCreateFunc)) {
                    logger.debug("表单处理完成.")
                } else {
                    throw BadRequestException("表单解析出现问题.")
                }
            } else {
                logger.debug("未找到适合[${headers["Content-Type"]}]的解析方案.")
            }
        } else {
            logger.warn("此会话并非 POST 请求，此方法不应该被调用.")
        }
        pointer = RequestPointer.END
    }

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
