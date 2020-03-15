package tech.openedgn.net.server.web.utils.dataBlock

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.ClosedException
import tech.openedgn.net.server.web.WebServer
import java.io.*

/**
 * 自动化创建存储块
 *
 * @property tempFile File 临时文件位置
 * @property maxMemorySize Int 超过此值就保存到文件中.
 * @property openDataReaderHook 内部触发器逻辑
 *
 */
class DataBlockOutputStream(
    private val tempFile: File = File.createTempFile("DataBlockOutputStream", System.nanoTime().toString()),
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

    @Volatile
    private var output: OutputStream = ByteArrayOutputStream(maxMemorySize)

    private var closed = false
    // 是否结束的指示

    private var open = false

    private var checkFile = false
    // 是否将缓存切换成本地储存

    private var size = 0L
    // 内存的缓存大小

    private fun checkSize(i: Int) {
        if (closed) {
            throw ClosedException("OutputStream Closed.")
        }
        size += i
        if (size > maxMemorySize && checkFile.not()) {
            checkFile = true
            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write((output as ByteArrayOutputStream).toByteArray())
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
