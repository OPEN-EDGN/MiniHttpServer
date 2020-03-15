package tech.openedgn.net.server.web.bean

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.utils.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.IDataBlock
import java.io.Closeable

data class FormItem (val data: IDataBlock):Closeable{
    val formItemHeaders = HashMap<String,String>()
    override fun close() {
        formItemHeaders.clear()
        data.safeClose()
    }
}