package tech.openedgn.net.server.web.utils

import tech.openedgn.net.server.web.data.FormItem
import java.net.URLDecoder

object DecodeUtils {
    fun decodeFormData(data: String, forms: HashMap<String, FormItem>, logger: WebLogger) {
        val urlDataSpit = data.split("&")
        for (dataItem in urlDataSpit) {
            dataItem.split("=").let {
                if (it.size == 2) {
                    val name = urlDecode(it[0])
                    forms[name] =  FormItem(urlDecode(it[1] ).createDataReader())
                } else {
                    logger.warn("表单键值对下有一对数据无法解析:[$dataItem]")
                }
            }
        }
    }

    fun urlDecode(data: String): String = URLDecoder.decode(data, "utf-8")
}
