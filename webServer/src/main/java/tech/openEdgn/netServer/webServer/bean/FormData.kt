package tech.openEdgn.netServer.webServer.bean

import tech.openEdgn.tools4k.METHOD
import tech.openEdgn.tools4k.calculatedHash
import tech.openEdgn.tools4k.md5sum
import tech.openEdgn.tools4k.safeClose
import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

interface BaseFormData:Closeable{
    val name:String

    val size:Long

    fun openInputStream(): InputStream

    open fun toString(charset: Charset): String {
        return openInputStream().readBytes().toString(charset)
    }

    fun toPrintString():String{
        return "${javaClass.simpleName}[name=\"$name\",md5=\"${openInputStream().calculatedHash(METHOD.MD5)}\"]"
    }

}

class StringFormData(override val name:String, private val data:String):BaseFormData{

    override val size: Long = data.toByteArray(Charsets.UTF_8).size.toLong()

    override fun openInputStream() = data.toByteArray(Charsets.UTF_8).inputStream()

    override fun toString(charset: Charset) :String{
        return if (charset == Charsets.UTF_8){
            data
        }else{
            super.toString(charset)
        }
    }

    override fun close() {
    }

    override fun toPrintString(): String {
        return "StringFormData[name=\"$name\",data=\"${data}\"]"
    }

}

/**
 *  注意，这为临时文件方案，文件会自动删除
 */
class FileFormData(override val name:String,private val file:File):BaseFormData{
    init {
        if (file.isFile.not()) {
            throw FileNotFoundException(file.absolutePath)
        }
        file.deleteOnExit()
    }
    private val list  = LinkedList<FileInputStream>()
    override val size = file.length()
    @Volatile
    private var closed = false
    @Synchronized
    override fun openInputStream(): InputStream {
        if (closed){
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
    }
    override fun toPrintString():String{
        return "${javaClass.simpleName}[name=\"$name\",path=\"${file.absolutePath}\",md5=\"${openInputStream().calculatedHash(METHOD.MD5)}\"]"
    }
}

fun File.createFormData(name:String) = FileFormData(name,this)