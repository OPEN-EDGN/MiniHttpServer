package tech.openedgn.net.server.web.request

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.utils.dataBlock.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import tech.openedgn.net.server.web.utils.safeCloseIt
import java.io.Closeable

/**
 *  表单实体类
 * @property data IDataBlock  表单数据
 * @property formItemHeaders HashMap<String, String> 表单头部信息
 */
data class FormItem (val data: IDataBlock):Closeable{
    val formItemHeaders = HashMap<String,String>()
    override fun close() {
        formItemHeaders.clear()
        data.safeCloseIt()
    }
}