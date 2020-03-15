package tech.openedgn.net.server.web.request.reader

import tech.openedgn.net.server.web.WebServer
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.bean.FormItem
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.utils.dataBlock.DataBlockOutputStream
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import tech.openedgn.net.server.web.utils.getWebLogger
import tech.openedgn.net.server.web.utils.safeCloseIt
import java.io.File
import java.util.*
import kotlin.collections.HashMap

abstract class BaseRequestReader(
    final override val remoteAddress: NetworkInfo,
    protected val webConfig: WebConfig
)  : IRequestReader {
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
         DataBlockOutputStream(
            File(
                webConfig.tempFolder,
                "tmp-${remoteAddress.ip}-${remoteAddress.port}-$sessionId-$it-${System.nanoTime()}.tmp"
            ),
            WebServer.MEMORY_CACHE_SIZE
        )
    }

    /**
     * 日志输出
     */
    protected val logger = getWebLogger(javaClass,remoteAddress.toString())



    @Synchronized
    override fun close() {
        headers.clear()
        // 注册清空 header的事件
        rawFormData.safeCloseIt(logger)
        // 原始表单数据的清除
        forms.values.forEach {
            it.safeCloseIt(logger)
            // 清空 POST 表单
        }
        forms.clear()
    }

}
