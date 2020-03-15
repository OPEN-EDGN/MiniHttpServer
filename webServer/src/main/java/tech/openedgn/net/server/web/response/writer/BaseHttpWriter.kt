package tech.openedgn.net.server.web.response.writer

import tech.openedgn.net.server.web.response.IResponse
import java.io.Closeable
import java.io.IOException
import java.io.OutputStream

abstract class BaseHttpWriter(protected val output:OutputStream) :Closeable{

    /**
     * 响应数据 （将来源信息写入）
     * @param response IResponse
     * @throws IOException
     */
    @Throws(IOException::class)
    abstract fun write(response: IResponse)

}