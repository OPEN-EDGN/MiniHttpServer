package tech.openedgn.net.server.web.request.reader

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.bean.FormItem
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.utils.ClosedManager
import tech.openedgn.net.server.web.utils.dataBlock.DataBlockOutputStream
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import tech.openedgn.net.server.web.utils.WebLogger
import java.io.Closeable
import java.io.File
import java.util.*
import kotlin.collections.HashMap

abstract class BaseRequestReader(
    final override val remoteAddress: NetworkInfo,
    protected val webConfig: WebConfig
) : ClosedManager(remoteAddress.toString()) ,
    IRequestReader,Closeable {
    override val sessionId: String = UUID.randomUUID().toString()
    override lateinit var method: METHOD
    override lateinit var location: String
    override lateinit var httpVersion: String
    override val headers = HashMap<String, String>()
    override val forms = HashMap<String, FormItem>()
    override lateinit var rawFormData: IDataBlock

    /**
     * 临时數據塊存儲方案
     */
    protected val tempDataBlockConstructorFun: (name: String) -> DataBlockOutputStream = {
        val dataReaderOutputStream = DataBlockOutputStream(
            File(webConfig.tempFolder, "tmp-${remoteAddress.ip}-${remoteAddress.port}-$sessionId-$it-${System.nanoTime()}.tmp"),
            WebServer.MEMORY_CACHE_SIZE
        ) { d -> d.registerCloseable() }
        dataReaderOutputStream.registerCloseable()
    }

    /**
     * 日志输出
     */
    protected val logger = WebLogger(javaClass)

    init {
        // 注册销毁表单的事件
        logger.remoteAddress = remoteAddress.toString()
        // 日志分块
    }


    @Synchronized
    override fun close() {
        super.closeAllRegisterCloseable()
        headers.clear()
        // 注册清空 header的事件
        rawFormData.safeClose()
        // 原始表单数据的清除
        forms.values.forEach {
            it.safeClose()
            // 清空 POST 表单
        }
        forms.clear()
    }

}
