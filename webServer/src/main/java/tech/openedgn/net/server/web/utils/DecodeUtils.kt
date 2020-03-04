package tech.openedgn.net.server.web.utils

import java.net.URLDecoder
import java.nio.charset.Charset

fun WebLogger.decodeFormData(data: String, dataBlock: HashMap<String, IDataBlock>, charset: Charset) {
    val urlDataSpit = data.split("&")
    for (dataItem in urlDataSpit) {
        dataItem.split("=").let {
            if (it.size == 2) {
                val name = URLDecoder.decode(it[0], charset.name())
                dataBlock[name] = URLDecoder.decode(it[1], charset.name()).createDataReader()
            } else {
                warn("表单键值对下有一对数据无法解析:[$dataItem]")
            }
        }
    }
}
