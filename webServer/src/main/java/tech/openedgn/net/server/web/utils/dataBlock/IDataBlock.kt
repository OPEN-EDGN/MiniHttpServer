package tech.openedgn.net.server.web.utils.dataBlock

import tech.openEdgn.tools4k.readText
import java.io.Closeable
import java.io.InputStream
import java.nio.charset.Charset

/**
 * 数据块模型
 *
 * 通过此数据块创建的流将受到其管理，
 * 可以将其看作 ByteArray
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


    /**
     * 克隆新的数据块
     *
     * @param offset Long 数据块偏移
     * @param length Long 新的长度
     * @return IDataBlock 克隆的长度
     */
    fun copyInto(
        offset: Long = 0,
        length: Long = size
    ): IDataBlock

    fun toString(start: Long, len: Int, charset: Charset): String{
        val inputStream = openInputStream()
        inputStream.skip(start)
        val array = ByteArray(len)
        val result = String(array, 0, inputStream.read(array), charset)
        inputStream.close()
        return result
    }
}