package tech.openedgn.net.server.web.io

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.utils.BaseDataReader
import tech.openedgn.net.server.web.utils.WebLogger
import java.io.Closeable
import java.io.IOException
import java.util.LinkedList
import kotlin.collections.HashMap

abstract class BaseRequestReader : IRequestReader {
    override lateinit var sessionId: String
    override lateinit var method: METHOD
    override lateinit var location: String
    override lateinit var httpVersion: String
    override val headers = HashMap<String, String>()
    override val forms = HashMap<String, BaseDataReader>()
    /**
     * 自动销毁模块寄存
     */
    protected val closeList = LinkedList<Closeable>()
    /**
     * 此对象是否关闭
     */
    @Volatile
    protected var closeable = false
    /**
     * 日志输出
     */
    protected val logger = WebLogger(javaClass)

    init {
        Closeable {
            headers.clear()
        }.registerCloseable()
        // 注册清空 header的事件
        Closeable {
            forms.values.forEach {
                it.safeClose()
            }
            forms.clear()
        }.registerCloseable()
        // 注册销毁表单的事件
    }

    protected fun <T : Closeable> T.registerCloseable(): T {
        if (closeable) {
            throw ClosedException("此对象生命周期已经结束！")
        }
        synchronized(closeList) {
            closeList.addFirst(this)
        }
        return this
    }

    override fun close() {
        closeable = true
        closeList.forEach {
            it.safeClose()
        } // 销毁对象
        closeList.clear()
    }

    class ClosedException(message: String) : IOException(message)
}
