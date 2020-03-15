package tech.openedgn.net.server.web.request.bodyLoader

import tech.openedgn.net.server.web.bean.FormItem
import tech.openedgn.net.server.web.BadRequestException
import tech.openedgn.net.server.web.utils.*
import tech.openedgn.net.server.web.utils.dataBlock.*
import java.util.*
import kotlin.collections.HashMap


/**
 *  读取 Content-Type 为  multipart/form-data 的数据
 *
 *  一个最简单的表单如下所示：
 *
 *  ```http
 *   Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
 *   \r\n
 *   ------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
 *   Content-Disposition: form-data; name="text_form"\r\n
 *   \r\n
 *   data\r\n
 *   ------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n
 *   Content-Disposition: form-data; name="file_form"; filename="hello.txt"\r\n
 *   Content-Type: text/plain\r\n
 *   \r\n
 *   data\r\n
 *   ------WebKitFormBoundary7MA4YWxkTrZu0gW--
 *  ```
 *
 */
class FormDataBodyLoader(logger: WebLogger) : BaseBodyLoader(logger) {

    override fun load(
        location: String,
        header: Map<String, String>,
        dataBlock: IDataBlock,
        forms: HashMap<String, FormItem>,
        tempDataBlockConstructorFun: (name: String) -> DataBlockOutputStream
    ): Boolean {
        val contentType = header.getOrElse("Content-Type", {
            throw BadRequestException("未发现Content-Type字段.")
        })
        val boundarySpit = contentType.split(Regex("boundary="), 2)
        if (boundarySpit.size != 2) {
            throw  BadRequestException("未发现Content-Type下boundary 字段 ( $boundarySpit ).")
        }

        val boundary = boundarySpit[1].trim()
        logger.debug("当前会话的 ContentType 为${boundarySpit[0]},表单的分割线则是：$boundary .")
        val spitBoundary = "\r\n--$boundary\r\n".toByteArray(Charsets.ISO_8859_1)
        val endBoundary = "\r\n--$boundary--".toByteArray(Charsets.ISO_8859_1)
        val indexList = LinkedList<Long>()
        var index = spitBoundary.size.toLong() - 2
        val startBoundaryStr = dataBlock.toString(0, index.toInt(), Charsets.ISO_8859_1)
        if (startBoundaryStr != "--$boundary\r\n") {
            throw BadRequestException("未知格式错误 [$startBoundaryStr]")
        }
        indexList.add(index)
        while (true) {
            index = dataBlock.searchIndex(spitBoundary, index)
            if (index == -1L) {
                break
            }
            val second = index + spitBoundary.size
            indexList.add(index)
            indexList.add(second)
            index = second
        }
        indexList.add(dataBlock.searchIndex(endBoundary, indexList.last))
        val cr2lf2 = "\r\n\r\n".toByteArray(Charsets.ISO_8859_1)
        for (i in (0 until indexList.size step 2)) {
            val formInfoEnd = dataBlock.searchIndex(cr2lf2, indexList[i], indexList[i + 1])
            val offset = formInfoEnd + cr2lf2.size
            if (formInfoEnd == -1L) {
                throw BadRequestException(
                    "在解析 ‘multipart/form-data’ 类型的表单中无法从此格式下得到正确的信息！[${dataBlock.copyInto(
                        indexList[i],
                        indexList[i + 1] - indexList[i]
                    )}]"
                )
            }
            val formBlock = dataBlock.copyInto(offset, indexList[i + 1] - offset, tempDataBlockConstructorFun)
            // 表单数据
            val formItemHeaderArrays =
                dataBlock.copyInto(indexList[i], formInfoEnd - indexList[i], tempDataBlockConstructorFun)
                    .toString(Charsets.ISO_8859_1)
                    .split("\r\n")
            val stringBuilder = StringBuilder()
            formItemHeaderArrays.forEach {
                stringBuilder.append(it).append(";")
            }
            val formItem = FormItem(formBlock)
            val hashMap = formItem.formItemHeaders
            val replace = stringBuilder
                .replace(Regex(":"), "=")
                .replace(Regex("(\"|\\s|;$)"),"")
            replace
                .split(";").forEach {
                    val spit = it.split(Regex("="), 2)
                    if (spit.size == 2) {
                        hashMap[spit[0].trim()] = DecodeUtils.urlDecode(spit[1].trim())
                    } else {
                        logger.debug("数据 [$it] 无法解析.")
                    }
                }
            val name = hashMap["name"] ?: throw BadRequestException("无法确定表单项名称!")
            forms[name] = formItem
        }
        return true
    }


}


