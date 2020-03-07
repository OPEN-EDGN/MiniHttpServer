package tech.openedgn.net.server.web.utils

import tech.openEdgn.tools4k.METHOD
import tech.openEdgn.tools4k.calculatedHash
import tech.openEdgn.tools4k.readText
import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.error.ClosedException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.RandomAccessFile
import java.nio.charset.Charset
import java.util.LinkedList

/**
 * 数据块模型
 *
 * 通过此数据块创建的流将受到其管理，
 * 可以将其看作等长 ByteArray
 *
 * @property size Long 数据块长度
 * @property text String  全部内容转换成
 */
interface IDataBlock : Closeable {

    val size: Long

    val text: String
        get() = toString(Charsets.UTF_8)

    /**
     * 创建一个全新的 `InputStream`
     *
     * 此`InputStream`受到当前 `#close()` 方法管理
     *
     * @return InputStream 新的 `InputStream` 对象
     */
    fun openInputStream(): InputStream

    /**
     * 将全部数据转换成字符串
     * @param charset Charset 编码类型
     * @return String 原始字符串
     */
    fun toString(charset: Charset): String {
        return openInputStream().readText(charset)
    }

    /**
     * 得到指定索引的Byte 数据
     * @param index Long 索引值
     */
    @Throws(IndexOutOfBoundsException::class)
    operator fun get(index: Long): Byte
}

/**
 *
 * ByteArray 类型的数据块
 *
 * @property data ByteArray 数据容器
 */
class ByteArrayDataBlock(
    private val data: ByteArray
) : IDataBlock {
    companion object {
        const val MAX_TO_STRING_SIZE = 50
    }

    private var closeable = false
    override val size: Long = data.size.toLong()
    override fun openInputStream() = run {
        if (closeable) {
            throw ClosedException("此对象已经关闭！")
        }
        ByteArrayInputStream(data)
    }

    override fun toString(charset: Charset): String {
        if (closeable) {
            throw ClosedException("此对象已经关闭！")
        }
        return data.toString(charset)
    }

    override fun get(index: Long) = run {
        if (closeable) {
            throw ClosedException("此对象已经关闭！")
        }
        data[index.toInt()]
    }

    override fun close() {
        closeable = true
    }

    override fun toString(): String {
        val info = if (size < MAX_TO_STRING_SIZE) {
            "data=\"$text\""
        } else {
            "SHA-256=\"${openInputStream().calculatedHash(METHOD.SHA256)}\""
        }
        return "${javaClass.simpleName}(dataLength=\"${data.size}\",$info)"
    }
}

/**
 * 文件类型的数据块
 *
 * @property blockFile File 数据块文件
 * @property deleteFile Boolean 是否自动删除
 */
class FileDataBlock(
    private val blockFile: File,
    private val deleteFile: Boolean = false
) : IDataBlock {
    private val randomAccessFile by lazy { RandomAccessFile(blockFile, "r") }
    private val closedList = LinkedList<Closeable>()

    init {
        if (blockFile.isFile.not()) {
            throw FileNotFoundException(blockFile.absolutePath)
        }
        closedList.addFirst(randomAccessFile)
    }

    override val size = blockFile.length()
    @Volatile
    private var closed = false

    @Synchronized
    override fun openInputStream(): InputStream {
        if (closed) {
            throw ClosedException("data closed.")
        }
        val inputStream = blockFile.inputStream()
        closedList.add(inputStream)
        return inputStream
    }

    @Synchronized
    override fun close() {
        closed = true
        closedList.forEach {
            it.safeClose()
        }
        closedList.clear()
        if (deleteFile && blockFile.delete().not()) {
            blockFile.deleteOnExit()
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(path=\"${blockFile.absolutePath}\"," +
                "SHA-256=\"${openInputStream().calculatedHash(METHOD.SHA256)}\")"
    }

    override fun get(index: Long): Byte {
        if (index > size) {
            throw IndexOutOfBoundsException("$index > block.size")
        }
        return synchronized(randomAccessFile) {
            randomAccessFile.seek(index)
            randomAccessFile.read().toByte()
        }
    }
}

// fun File.createDataReader() = FileDataBlock(this)

fun String.createDataReader() =
    ByteArrayDataBlock(this.toByteArray(Charsets.UTF_8))

/**
 * 自动化创建存储块
 *
 * @property tempFile File 临时文件位置
 * @property maxMemorySize Int 超过此值就保存到文件中.
 * @property openDataReaderHook 内部触发器逻辑
 *
 */
class DataBlockOutputStream(
    private val tempFile: File,
    private val maxMemorySize: Int = WebServer.CACHE_SIZE,
    private val openDataReaderHook: (IDataBlock) -> Unit = {}
) : OutputStream() {

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

    private var open = false

    private var checkFile = false
    // 是否将缓存切换成本地储存

    private var size = 0
    // 内存的缓存大小

    private fun checkSize(i: Int) {
        if (closed) {
            throw ClosedException("OutputStream Closed.")
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

    private val dataBlock: IDataBlock by lazy {
        synchronized(lock) {
            open = true
            close()
            val baseDataReader = if (checkFile) {
                FileDataBlock(tempFile, true)
            } else {
                ByteArrayDataBlock((output as ByteArrayOutputStream).toByteArray())
            }
            openDataReaderHook(baseDataReader)
            // 触发器HOOK
            baseDataReader
        }
    }

    /**
     * 当执行此方法时，将自动关闭此 `OutputStream `
     * 多次执行共享同一实例
     *
     * @return BaseDataReader
     */
    fun openDataReader() = dataBlock

    override fun close() {
        synchronized(lock) {
            if (!closed) {
                output.safeClose()
                closed = true
                if (checkFile.not()) {
                    tempFile.delete()
                }
                if (open.not()) {
                    tempFile.delete()
                }
            }
        }
    }
}