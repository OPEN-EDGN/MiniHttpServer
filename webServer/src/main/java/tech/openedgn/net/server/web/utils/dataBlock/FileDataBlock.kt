package tech.openedgn.net.server.web.utils.dataBlock

import tech.openEdgn.tools4k.METHOD
import tech.openEdgn.tools4k.calculatedHash
import tech.openedgn.net.server.web.ClosedException
import tech.openedgn.net.server.web.utils.ClosedManager
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * 文件类型的数据块
 *
 * @property blockFile File 数据块文件
 */
class FileDataBlock(
    private val blockFile: File
) : ClosedManager("", true), IDataBlock {
    private val randomAccessFile by lazy { RandomAccessFile(blockFile, "r") }

    init {
        if (blockFile.isFile.not()) {
            throw FileNotFoundException(blockFile.absolutePath)
        }
        randomAccessFile.registerCloseable()
    }

    override val size = blockFile.length()
    @Volatile
    private var closed = false

    @Synchronized
    override fun openInputStream(): InputStream {
        if (closed) {
            throw ClosedException("data closed.")
        }
        return blockFile.inputStream().registerCloseable()
    }

    override fun copyInto(offset: Long, length: Long): IDataBlock {
        if (closed) {
            throw ClosedException("data closed.")
        }
        if ((offset + length) >= size) {
            throw IndexOutOfBoundsException("(offset + length) >= size")
        }
        val blockOutputStream = DataBlockOutputStream(File("${blockFile.absolutePath}-clone-$offset-$length"))
        val inputStream = openInputStream()
        inputStream.skip(offset)
        var copiedSize: Long = length
        val bufferSize = 4096
        val buffer = ByteArray(bufferSize)
        while (true) {
            if (copiedSize < bufferSize) {
                inputStream.read(buffer, 0, copiedSize.toInt())
                blockOutputStream.write(buffer, 0, copiedSize.toInt())
                break
            } else {
                copiedSize -= inputStream.read(buffer, 0, bufferSize)
                blockOutputStream.write(buffer, 0, bufferSize)
            }
        }
        return blockOutputStream.openDataReader()
    }

    @Synchronized
    override fun close() {
        closed = true
        super.closeAllRegisterCloseable()
        if (blockFile.delete().not()) {
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