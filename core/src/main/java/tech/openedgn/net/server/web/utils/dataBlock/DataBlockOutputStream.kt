package tech.openedgn.net.server.web.utils.dataBlock

import tech.openedgn.net.server.web.ClosedException
import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.utils.safeCloseIt
import java.io.*

/**
 *  缓存型 OutputStream
 *
 *  为尽量减少读写磁盘，减少磁盘的吞吐量，使用此 OutputStream 可达到效果，
 *  碎片数据保存在RAM ，而大文件则保存到临时目录，关于 `DataBlock` 的介绍请看 `IDataBlock`
 *
 * @property tempFile File 临时文件位置
 * @property maxMemorySize Int 缓存大小，超过此值就保存到文件中防止内存超标.
 *
 */
class DataBlockOutputStream(
    private val tempFile: File = File.createTempFile("DataBlockOutputStream", System.nanoTime().toString()),
    private val maxMemorySize: Int = WebServer.MEMORY_CACHE_SIZE
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
            closed = true
            output.safeCloseIt()
            val baseDataReader = if (checkFile) {
                TempFileDataBlock(tempFile)
            } else {
                tempFile.delete()
                ByteArrayDataBlock((output as ByteArrayOutputStream).toByteArray())
            }
            baseDataReader
        }
    }

    /**
     * 当执行此方法时，将自动关闭此 `OutputStream `
     * 共享同一实例
     *
     * @return BaseDataReader
     */
    fun openDataReader() = dataBlock

    /**
     * 执行此方法会导致创建的`DataBlock` 销毁！
     */
    override fun close() {
        synchronized(lock) {
            if (!closed) {
                output.safeCloseIt()
                closed = true
                if (checkFile.not()) {
                    tempFile.delete()
                }
                if (open) {
                    dataBlock.safeCloseIt()
                }else{
                    tempFile.delete()
                }
            }
        }
    }
}
