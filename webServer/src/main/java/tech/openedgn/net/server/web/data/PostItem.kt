package tech.openedgn.net.server.web.data

import tech.openEdgn.tools4k.safeClose
import tech.openedgn.net.server.web.utils.ByteArrayDataBlock
import tech.openedgn.net.server.web.utils.IDataBlock
import java.io.Closeable

class PostItem (val name:String,val data: IDataBlock):Closeable{
    override fun close() {

        data.safeClose()
    }


}