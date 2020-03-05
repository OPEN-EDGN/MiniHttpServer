package tech.openedgn.net.server.web.utils

import java.net.URLDecoder
import java.nio.charset.Charset

object DecodeUtils {
    fun decodeFormData(data: String, dataBlock: HashMap<String, IDataBlock>, logger: WebLogger) {
        val urlDataSpit = data.split("&")
        for (dataItem in urlDataSpit) {
            dataItem.split("=").let {
                if (it.size == 2) {
                    val name = urlDecode(it[0])
                    dataBlock[name] =  urlDecode(it[1] ).createDataReader()
                } else {
                    logger.warn("表单键值对下有一对数据无法解析:[$dataItem]")
                }
            }
        }
    }

    fun urlDecode(data: String) = URLDecoder.decode(data, "utf-8")
}
