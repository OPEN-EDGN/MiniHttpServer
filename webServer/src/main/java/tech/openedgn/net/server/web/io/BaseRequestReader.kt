package tech.openedgn.net.server.web.io

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.config.WebConfig
import tech.openedgn.net.server.web.data.METHOD
import tech.openedgn.net.server.web.error.ClosedException
import tech.openedgn.net.server.web.utils.DataBlockOutputStream
import tech.openedgn.net.server.web.utils.IDataBlock
import tech.openedgn.net.server.web.utils.WebLogger
import java.io.Closeable
import java.io.File
import java.util.LinkedList
import java.util.UUID
import kotlin.collections.HashMap

abstract class BaseRequestReader(
    final override val remoteAddress: NetworkInfo,
    protected val webConfig: WebConfig
) : IRequestReader {
    override val sessionId: String = UUID.randomUUID().toString()
    override lateinit var method: METHOD
    override lateinit var location: String
    override lateinit var httpVersion: String
    override val headers = HashMap<String, String>()
    override val forms = HashMap<String, IDataBlock>()
    override lateinit var rawFormData: IDataBlock
    /**
     * 自动销毁模块寄存
     */
    protected val closeList = LinkedList<Closeable>()
    /**
     * 临时數據塊存儲方案
     */
    protected val tempBlockCreateFunc: (name: String) -> DataBlockOutputStream = {
        val dataReaderOutputStream = DataBlockOutputStream(
            File(webConfig.tempFolder, "temp-$sessionId-$it-${System.nanoTime()}.tmp"),
            WebServer.MEMORY_CACHE_SIZE
        ) { it.registerCloseable() }
        dataReaderOutputStream.registerCloseable()
    }
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
            rawFormData.safeClose()
            // 原始表单数据的清除
            forms.values.forEach {
                it.safeClose()
            }
            forms.clear()
        }.registerCloseable()
        // 注册销毁表单的事件
        logger.remoteAddress = remoteAddress.toString()
        // 日志分块
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
}
