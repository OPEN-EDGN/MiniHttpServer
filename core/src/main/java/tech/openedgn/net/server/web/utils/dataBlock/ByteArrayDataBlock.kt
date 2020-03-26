package tech.openedgn.net.server.web.utils.dataBlock

import tech.openEdgn.tools4k.METHOD
import tech.openEdgn.tools4k.calculatedHash
import tech.openedgn.net.server.web.ClosedException
import java.io.ByteArrayInputStream
import java.nio.charset.Charset

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

    override fun copyInto(offset: Long, length: Long): IDataBlock {
        if (closeable) {
            throw ClosedException("此对象已经关闭！")
        }
        if ((offset + length) >= size) {
            throw IndexOutOfBoundsException("(offset + length) >= size")
        }
        val result = ByteArray(length.toInt())
        System.arraycopy(data, offset.toInt(), result, 0, length.toInt())
        return ByteArrayDataBlock(result)
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