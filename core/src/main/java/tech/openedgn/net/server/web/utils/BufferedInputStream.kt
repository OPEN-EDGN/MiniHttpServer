package tech.openedgn.net.server.web.utils

import tech.openEdgn.tools4k.safeClose
import java.io.ByteArrayOutputStream
import java.io.InputStream

// ISO-8859-1 编码是单字节编码，向下兼容ASCII，其编码范围是0x00-0xFF，0x00-0x7F之间完全和ASCII一致，0x80-0x9F之间是控制字符，0xA0-0xFF之间是文字符号。
/**
 *  从字节流中读取字符或行
 *
 * 注意: 由于不存在缓冲区，效率较为低下
 *
 * @property input InputStream  目标字节流
 * @constructor
 */
class BufferedInputStream(private val input: InputStream) : InputStream() {
    private val byteCR = '\r'.toByte()
    private val byteLF = '\n'.toByte()
    private val lock = input
    private var acceptByteSize: Long = 0
    private val charset = Charsets.ISO_8859_1.name()

    /**
     * # 从流中读取一行以 `CRLF (\r\n)`  结尾的字符串
     *
     *  注意：只有 **符合** ` CRLF (\r\n)` 才会被认定为一行
     *
     * @param addCRLFChar Boolean 是否略过不符合条件但为换行字符的`Byte`
     * @return String?  读取的数据 ，如果无法读取数据则会返回 NULL
     */
    @JvmOverloads
    fun readLineEndWithCRLF(addCRLFChar: Boolean = false): String? {
        synchronized(lock) {
            if (available() <= 0) {
                return null
            }
            var byte: Byte
            var isCR = false
            val outputStream = ByteArrayOutputStream()
            while (available() > 0) {
                byte = read().toByte()
                if (byte == byteLF && isCR) {
                    break
                }
                isCR = byte == byteCR
                if (addCRLFChar && (byte == byteCR || byte == byteLF)) {
                    outputStream.write(byte.toInt())
                } else if (byte != byteCR && byte != byteLF) {
                    outputStream.write(byte.toInt())
                }
            }
            val result = if (outputStream.size() == 0) {
                ""
            } else {
                outputStream.toString(charset)
            }
            outputStream.safeCloseIt()
            return result
        }
    }

    /**
     *  # 从流中读取一行字符串
     *
     * @param ignoreCR Boolean 是否略过 `CR （\r）`
     * @return String? 读取的数据 ，如果无法读取数据则会返回 NULL
     */
    @JvmOverloads
    fun readLine(ignoreCR: Boolean = true): String? {
        synchronized(lock) {
            if (available() <= 0) {
                return null
            }
            val outputStream = ByteArrayOutputStream()
            var byte: Byte
            while (available() > 0) {
                byte = read().toByte()
                if (byte == byteLF || (byte == byteCR && ignoreCR.not())) {
                    break
                }
                if (byte == byteCR) {
                    continue
                }
                outputStream.write(byte.toInt())
            }
            val result = if (outputStream.size() == 0) {
                ""
            } else {
                outputStream.toString(charset)
            }
            outputStream.safeCloseIt()
            return result
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
