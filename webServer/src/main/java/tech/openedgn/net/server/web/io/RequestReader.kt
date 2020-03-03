package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.utils.BaseDataReader
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.data.MethodData
import tech.openedgn.net.server.web.error.HeaderFormatException
import tech.openedgn.net.server.web.error.MethodFormatException
import tech.openedgn.net.server.web.utils.BufferedInputStream
import tech.openedgn.net.server.web.utils.WebLogger
import tech.openedgn.net.server.web.utils.decodeFormData
import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.error.BadRequestException
import tech.openedgn.net.server.web.error.WebServerInternalException
import tech.openedgn.net.server.web.utils.ByteArrayDataReader
import tech.openedgn.net.server.web.utils.DataReaderOutputStream
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.Closeable
import java.io.File
import java.io.InputStream
import java.lang.NullPointerException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

class RequestReader(
    inputStream: InputStream,
    private val charset: Charset,
    private val networkInfo: NetworkInfo,
    private val tempFolder: File
) : Closeable {
    companion object {
        const val METHOD_SPIT_SIZE = 3
    }

    private val reader = BufferedInputStream(inputStream)

    private val logger = getWebLogger()
    /**
     * 会话 ID
     */
    val sessionId = UUID.randomUUID().toString()
    /**
     *  HTTP METHOD
     */
    lateinit var methodData: MethodData
    /**
     * HTTP 头部数据
     */
    val header = HashMap<String, String>()
    /**
     * 表单
     */
    val formData = HashMap<String, BaseDataReader>()
    /**
     * 原始的表单数据
     */
    lateinit var rawFormData: BaseDataReader

    private var bodyLoader: RequestBodyLoader? = null

    private val closeableLinkedList = LinkedList<Closeable>()

    protected fun <T : Closeable> T.autoClose(): T {
        synchronized(closeableLinkedList) {
            closeableLinkedList.addFirst(this)
        }
        return this
    }

    private val hookFunc: (BaseDataReader) -> Unit = { it.autoClose() }

    val tempBlockCreateFunc: (name: String) -> DataReaderOutputStream = {
        val dataReaderOutputStream = DataReaderOutputStream(
                File(tempFolder, "temp-$sessionId-$it-${System.nanoTime()}.tmp"),
                WebServer.CACHE_SIZE,
                hookFunc)
        dataReaderOutputStream.autoClose()
    }

    init {
        logger.remoteAddress = networkInfo.toString()
    }

    /**
     * 读取表头信息
     */
    fun loadHeader() {
        val methodLine = reader.readLineEndWithCRLF() ?: throw NullPointerException("METHOD 读取中断！")
        if (methodLine.matches(Regex("^(GET|POST)\\s(/)(.*)\\s\\w{4}(/).+$")).not()) {
            throw MethodFormatException(methodLine)
        }
        val methodSplit = methodLine.split(" ")
        if (methodSplit.size != METHOD_SPIT_SIZE) {
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
        // 解析出所有url下键值对
        if (method == METHOD.GET && urlData.isNotEmpty()) {
            logger.decodeFormData(urlData, formData, charset)
            // 解析 GET 附加表单
        } else if (urlData.isNotEmpty()) {
            logger.debug("在[$method]下已自动排除url中带的表单数据:{$urlData}")
            // POST 不会处理 URL 的附加数据
        } else {
            logger.debug("URL 中未发现无表单键值对.")
        }
        methodData = MethodData(
                method,
                URLDecoder.decode(location, charset.name()),
                methodSplit[2].toLowerCase()
        )
        rawFormData = ByteArrayDataReader(urlData.toByteArray())
        logger.info("ACCEPT [$methodData]")
        // 读取method 结束
        while (true) {
            val line = reader.readLineEndWithCRLF() ?: throw NullPointerException("Header 读取中断！")
            if (line.isEmpty()) {
                break
                // 代表Header已经全部读取完成
            }
            val headerSpit = line.split(Regex(":"), 2)
            if (headerSpit.size != 2) {
                throw HeaderFormatException(line)
            }
            header[headerSpit[0]] = URLDecoder.decode(headerSpit[1].trim(), charset.name())
        }
        printHeader()
    }

    /**
     * 解析 POST表单
     *
     * 此函数用于解析 HTTP 的请求表单
     *
     * @param loader Map<String, KClass<out RequestBodyLoader>> 解析方案
     */
    fun loadBody(loader: Map<String, KClass<out RequestBodyLoader>>) {
        //     开始读取 POST 表单数据
        if (methodData.type == METHOD.POST) {
            if (tempFolder.isDirectory.not()) {
                tempFolder.mkdirs()
            }
            val outputStream = tempBlockCreateFunc("post")
            val bytes = ByteArray(WebServer.CACHE_SIZE)
            while (reader.available() > 0) {
                outputStream.write(bytes, 0, reader.read(bytes, 0, bytes.size))
            }
            rawFormData = outputStream.openDataReader()
            header["Content-Length"]?.let {
                if (rawFormData.size < it.toLong()) {
                    throw BadRequestException("POST 表单的实际长度低于 HEADER 标明长度. [${rawFormData.size} < ${it.length}]")
                }
            }
        }
        if (methodData.type == METHOD.POST) {
            var findKClass: KClass<out RequestBodyLoader>? = null
            val contentType = header["Content-Type"] ?: throw BadRequestException("请求为POST但未知请求类型（未发现Content-Type字段）.")
            logger.debug("Content-Type:$contentType")
            synchronized(loader) {
                val keys = loader.keys
                // 解析POST 请求的表单
                logger.debugOnly {
                    it.debug("当前Content-Type解析方案：$keys")
                }
                for (key in keys) {
                    if (contentType.toLowerCase().contains(key)) {
                        findKClass = loader[key]
                        break
                    }
                }
            }
            if (findKClass == null) {
                logger.debug("未找到适合Content-Type解析方案:[$contentType].")
            } else {
                @SuppressWarnings("TooGenericExceptionCaught")
                // 压制 `deteKT` 的警告信息
                val requestBodyLoader = try {
                    val webLogger = WebLogger(findKClass!!.java)
                    webLogger.remoteAddress = logger.remoteAddress
                    findKClass!!.javaObjectType.getConstructor(WebLogger::class.java).newInstance(webLogger)
                } catch (e: Exception) {
                    throw WebServerInternalException("在创建表单解析方案时出现错误！", e)
                }
                bodyLoader = requestBodyLoader
                if (requestBodyLoader.load(methodData.location, header, rawFormData, formData, tempBlockCreateFunc)) {
                    logger.debug("表单处理完成.")
                    printBody()
                } else {
                    throw BadRequestException("表单解析错误，具体信息需查看日志.")
                }
            }

            //
        } else {
            logger.debug("执行到#loadBody()方法，但是此会话为 GET 请求.")
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught") // 压制 deteKt 警告信息
    override fun close() {
        bodyLoader.safeClose() // 销毁POST解析工具（如果有）
        closeableLinkedList.forEach {
            try {
                it.close()
            } catch (e: Exception) {
                logger.debug("$it 关闭失败！")
            }
        } // 销毁创建的临时文件
        closeableLinkedList.clear()
        rawFormData.safeClose() // 清空POST 表单
        synchronized(formData) {
            for (value in formData.values) {
                value.safeClose()
            }
        }
        header.clear()
        formData.clear()
        reader.safeClose()
    }

    private fun printHeader() {
        logger.debugOnly {
            header.forEach { (t, u) ->
                logger.debug("HEADER(name=\"$t\",value=\"$u\").")
            }
            logger.debug("HEADER.length:${header.size} .")
        }
    }

    private fun printBody() {
        logger.debugOnly {
            formData.forEach { (t, u) ->
                logger.debug("BODY(name=\"$t\",data=\"$u\")")
            }
            logger.debug("BODY.length:${formData.size} .")
            logger.debug("原始表单数据:$rawFormData")
        }
    }
}
