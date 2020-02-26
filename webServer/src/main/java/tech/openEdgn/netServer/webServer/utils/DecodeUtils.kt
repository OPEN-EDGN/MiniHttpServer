package tech.openEdgn.netServer.webServer.utils

import tech.openEdgn.netServer.webServer.bean.BaseDataReader
import tech.openEdgn.netServer.webServer.bean.createDataReader
import java.net.URLDecoder
import java.nio.charset.Charset

fun WebLogger.decodeFormData(data:String, dataReader:HashMap<String, BaseDataReader>, charset: Charset){
    val urlDataSpit = data.split("&")
    for (dataItem in urlDataSpit) {
        dataItem.split("=").let {
            if (it.size == 2) {
                val name = URLDecoder.decode(it[0], charset.name())
                dataReader[name] =  URLDecoder.decode(it[1], charset.name()).createDataReader()
            } else {
                warn("表单键值对下有一对数据无法解析:[$dataItem]")
            }
        }
    }
}