package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.utils.IDataBlock
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.data.MethodData
import tech.openedgn.net.server.web.error.HeaderFormatException
import tech.openedgn.net.server.web.error.MethodFormatException
import tech.openedgn.net.server.web.utils.BufferedInputStream
import tech.openedgn.net.server.web.utils.decodeFormData
import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.error.BadRequestException
import tech.openedgn.net.server.web.utils.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.DataBlockOutputStream
import tech.openedgn.net.server.web.utils.getWebLogger
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.LinkedList
import java.util.UUID

import kotlin.collections.HashMap
import kotlin.reflect.KClass

class OldRequestReader(
    inputStream: InputStream,
    private val charset: Charset,
    private val loggerTag: String,
    private val tempFolder: File
) : Closeable {
    companion object {
        const val METHOD_SPIT_SIZE = 3
    }

    private val logger = getWebLogger()

    init {
        logger.remoteAddress = loggerTag
    }

    private val reader = BufferedInputStream(inputStream)

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
    val formData = HashMap<String, IDataBlock>()
    /**
     * 原始的表单数据
     */
    lateinit var rawFormData: IDataBlock

    private var bodyLoaderBase: BaseRequestBodyLoader? = null

    private val closeableLinkedList = LinkedList<Closeable>()

    protected fun <T : Closeable> T.autoClose(): T {
        synchronized(closeableLinkedList) {
            closeableLinkedList.addFirst(this)
        }
        return this
    }

    private val hookFunc: (IDataBlock) -> Unit = { it.autoClose() }

    /**
     * 建立临时文件
     */
    val tempBlockCreateFunc: (name: String) -> DataBlockOutputStream = {
        val dataReaderOutputStream = DataBlockOutputStream(
                File(tempFolder, "temp-$sessionId-$it-${System.nanoTime()}.tmp"),
                WebServer.MEMORY_CACHE_SIZE,
                hookFunc)
        dataReaderOutputStream.autoClose()
    }

    /**
     * 读取表头信息
     */
    fun loadHeader() {
        readMethod(reader.readLineEndWithCRLF() ?: throw IOException("METHOD 读取中断！"))
        // 解析 METHOD
        logger.info("ACCEPT [$methodData]")
        readHeader()
        // 解析 HEADER 頭標籤
        printHeaderInfo()
        // 打印HEADER 信息
    }

    /**
     * 解析 POST表单
     *
     * 此函数用于解析 HTTP 的请求表单
     *
     * @param loaderBase Map<String, KClass<out RequestBodyLoader>> 解析方案
     */
    fun loadBody(loaderBase: Map<String, KClass<out BaseRequestBodyLoader>>) {
        //     开始读取 POST 表单数据
        if (methodData.type == METHOD.POST) {
            saveAllBodyData()
            decodeBodyData(loaderBase)
            printBodyInfo()
        } else {
            logger.debug("此会话为 GET 请求，不解析尾部表单.")
        }
    }

    /**
     * 从流中读取 METHOD 信息
     */
    fun readMethod(methodLine: String) {
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
        rawFormData = ByteArrayDataBlock(urlData.toByteArray())
        // 读取method 结束
    }

    /**
     * 读取 HEADER 信息
     */
    fun readHeader() {
        while (true) {
            val line = reader.readLineEndWithCRLF() ?: throw IOException("Header 读取中断！")
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
    }

    /**
     * 将原始的 POST 数据保存到缓存中
     */
    fun saveAllBodyData() {
        if (tempFolder.isDirectory.not()) {
            tempFolder.mkdirs()
        }
        // 建立临时文件
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

    /**
     * 解析表单信息
     */
    fun decodeBodyData(loaderBase: Map<String, KClass<out BaseRequestBodyLoader>>) {
        var findKClass: KClass<out BaseRequestBodyLoader>? = null
        val contentType = header["Content-Type"] ?: throw BadRequestException("请求为POST但未知请求类型（未发现Content-Type字段）.")
        logger.debug("Content-Type:$contentType")
        synchronized(loaderBase) {
            val keys = loaderBase.keys
            // 解析POST 请求的表单
            logger.debugOnly {
                it.debug("当前Content-Type解析方案：$keys")
            }
            for (key in keys) {
                if (contentType.toLowerCase().contains(key)) {
                    findKClass = loaderBase[key]
                    break
                }
            }
        }
        if (findKClass != null) {
            val requestBodyLoader = BaseRequestBodyLoader.createNewDataBodyLoader(findKClass!!, loggerTag)
            bodyLoaderBase = requestBodyLoader
            if (requestBodyLoader.load(methodData.location, header, rawFormData, formData, tempBlockCreateFunc)) {
                logger.debug("表单处理完成.")
            } else {
                throw BadRequestException("表单解析错误，具体信息需查看日志.")
            }
        } else {
            logger.debug("未找到适合Content-Type解析方案:[$contentType].")
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught") // 压制 deteKt 警告信息
    override fun close() {
        bodyLoaderBase.safeClose() // 销毁POST解析工具（如果有）
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

    /**
     * 打印 HEADER 日誌
     */
    private fun printHeaderInfo() {
        logger.debugOnly {
            header.forEach { (t, u) ->
                logger.debug("HEADER(name=\"$t\",value=\"$u\").")
            }
            logger.debug("HEADER.length:${header.size} .")
        }
    }

    /**
     * 打印表單數據
     */
    private fun printBodyInfo() {
        logger.debugOnly {
            formData.forEach { (t, u) ->
                logger.debug("BODY(name=\"$t\",data=\"$u\")")
            }
            logger.debug("BODY.length:${formData.size} .")
            logger.debug("原始表单数据:$rawFormData")
        }
    }
}
