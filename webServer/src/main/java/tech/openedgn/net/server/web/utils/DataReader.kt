package tech.openedgn.net.server.web.utils

import tech.openEdgn.tools4k.METHOD
import tech.openEdgn.tools4k.calculatedHash
import tech.openEdgn.tools4k.readText
import tech.openEdgn.tools4k.safeClose
import java.io.*
import java.nio.charset.Charset
import java.util.*

interface BaseDataReader : Closeable {

    val size: Long

    fun openInputStream(): InputStream

    val text: String
        get() = toString(Charsets.UTF_8)

    open fun toString(charset: Charset): String {
        return openInputStream().readText(charset)
    }
}

class ByteArrayDataReader(private val data: ByteArray) :
    BaseDataReader {

    override val size: Long = data.size.toLong()

    override fun openInputStream() = ByteArrayInputStream(data)

    override fun toString(charset: Charset): String {
        return data.toString(charset)
    }

    override fun close() {}

    override fun toString(): String {
        val info = if (size < 50) {
            "data=\"$text\""
        } else {
            "MD5=\"${openInputStream().calculatedHash(METHOD.MD5)}\""
        }
        return "${javaClass.simpleName}(dataLength=\"${data.size}\",$info)"
    }
}

/**
 *  注意，这为临时文件方案，文件会自动删除
 */
class FileDataReader(private val file: File) :
    BaseDataReader {
    init {
        if (file.isFile.not()) {
            throw FileNotFoundException(file.absolutePath)
        }
        file.deleteOnExit()
    }

    private val list = LinkedList<FileInputStream>()
    override val size = file.length()
    @Volatile
    private var closed = false

    @Synchronized
    override fun openInputStream(): InputStream {
        if (closed) {
            throw IOException("data closed.")
        }
        val inputStream = file.inputStream()
        list.add(inputStream)
        return inputStream
    }

    @Synchronized
    override fun close() {
        closed = true
        list.forEach {
            it.safeClose()
        }
        list.clear()
        if (file.delete().not()) {
            file.deleteOnExit()
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(path=\"${file.absolutePath}\"," +
                "MD5=\"${openInputStream().calculatedHash(METHOD.MD5)}\")"
    }
}

fun File.createDataReader() = FileDataReader(this)

fun String.createDataReader() =
    ByteArrayDataReader(this.toByteArray(Charsets.UTF_8))

/**
 * 自动化创建存储块
 *
 * @property tempFile File 临时文件位置
 * @property maxMemorySize Int 超过此值就保存到文件中.
 *
 */
class DataReaderOutputStream(private val tempFile: File, private val maxMemorySize: Int = 8182) : OutputStream() {

    init {
        if (tempFile.isFile && tempFile.length() > 0) {
            throw IOException("文件非空！")
        }
        tempFile.delete()
        if (tempFile.createNewFile().not()) {
            throw IOException("文件创建失败.")
        }
    }

    private val lock = Any()

    private var output: OutputStream = ByteArrayOutputStream(maxMemorySize)

    private var closed = false
    // 是否结束的指示

    private var checkFile = false
    // 是否将缓存切换成本地储存

    private var size = 0
    // 内存的缓存大小

    private fun checkSize(i: Int) {
        if (closed) {
            throw IOException("OutputStream Closed.")
        }
        if (size < maxMemorySize) {
            size += i
        }
        if (size > maxMemorySize && checkFile.not()) {
            checkFile = true
            val fileOutputStream = FileOutputStream(tempFile)
            (output as ByteArrayOutputStream).writeTo(fileOutputStream)
            output = fileOutputStream
        }
    }

    override fun write(b: Int) {
        synchronized(lock) {
            checkSize(1)
            output.write(b)
        }
    }

    override fun write(b: ByteArray) {
        synchronized(lock) {
            checkSize(b.size)
            output.write(b)
        }
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        synchronized(lock) {
            checkSize(len)
            output.write(b, off, len)
        }
    }

    override fun flush() {
        synchronized(lock) {
            output.flush()
        }
    }

    private val dataReader: BaseDataReader by lazy {
        synchronized(lock) {
            close()
            if (checkFile) {
                FileDataReader(tempFile)
            } else {
                ByteArrayDataReader((output as ByteArrayOutputStream).toByteArray())
            }
        }
    }

    /**
     * 当执行此方法时，将自动关闭此 `OutputStream `
     * 多次执行共享同一实例
     *
     * @return BaseDataReader
     */
    fun openDataReader() = dataReader

    override fun close() {
        synchronized(lock) {
            if (closed) {
                return
            }
            output.safeClose()
            closed = true
            if (checkFile.not()) {
                tempFile.delete()
            }
        }
    }
}
