package tech.openedgn.net.server.web.bean

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.utils.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.IDataBlock
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
        data.safeClose()
    }
}