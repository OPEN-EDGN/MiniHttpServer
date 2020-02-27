package tech.openedgn.net.server.web.utils

import java.io.InputStream
import java.lang.StringBuilder

/**
 *
 * ISO-8859-1 编码是单字节编码，向下兼容ASCII，其编码范围是0x00-0xFF，0x00-0x7F之间完全和ASCII一致，0x80-0x9F之间是控制字符，0xA0-0xFF之间是文字符号。
 */
class BufferedInputStream(private val input: InputStream) : InputStream() {
    private val charCr = '\r'.toByte()
    private val charLf = '\n'.toByte()
    private val lock = input
    private var acceptByteSize: Long = 0

    /**
     *  只有符合 ` CRLF (\r\n)` 才会被认定为一行
     *
     * @param addCRLF 是否保留行中的单个CR和LF
     *
     * @return String? 如果无法读取数据则会返回 NULL
     */
    fun readLine(addCRLF: Boolean = false): String? {
        synchronized(lock) {
            if (available() <= 0) {
                return null
            }
            var byte: Byte
            var findCR = false
            val buff = StringBuilder()
            while (true) {
                if (available() <= 0) {
                    break
                }
                byte = read().toByte()

                if (byte == charLf && findCR) {
                    break
                } // 如果符合条件

                if (((byte == charLf && addCRLF.not()) || (byte == charCr && findCR && addCRLF.not()) || (byte == charCr && addCRLF.not())).not()) {
                    buff.append(byte.toChar())
                }
                findCR = byte == charCr
            }
            return buff.toString()
        }
    }

    private fun acceptByteSize(size: Int): Int {
        acceptByteSize += size
        return size
    }

    private fun acceptByteSize(size: Long): Long {
        acceptByteSize += size
        return size
    }

    override fun close() {
        input.close()
    }

    override fun read(b: ByteArray): Int {
        return synchronized(lock) {
            acceptByteSize(input.read(b))
        }
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        return synchronized(lock) {
            acceptByteSize(input.read(b, off, len))
        }
    }

    override fun skip(n: Long): Long {
        return synchronized(lock) {
            acceptByteSize(input.skip(n))
        }
    }

    override fun available(): Int {
        return synchronized(lock) {
            input.available()
        }
    }

    override fun read(): Int {
        return synchronized(lock) {
            val read = input.read()
            acceptByteSize(1)
            read
        }
    }
}
