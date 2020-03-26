package tech.openedgn.net.server.web.consts

import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tech.openEdgn.tools4k.readText
import java.io.File
import kotlin.collections.HashMap

class ContentTypeTest {

    @DisplayName("测试通过文件名称得到Content-Type")
    @Test
    fun contentTypeTestA() {
        assertEquals(ContentType.getFileNameContentType("music.mp3"), ContentType.TYPE_MP3)
        assertEquals(ContentType.getFileNameContentType(".mp6"), ContentType.TYPE_OCTET_STREAM)
    }

    @DisplayName("测试通过ContentType字段得到Content-Type")
    @Test
    fun contentTypeTestB() {
        assertEquals(ContentType.getContentType("audio/mp3"), ContentType.TYPE_MP3)
        assertEquals(ContentType.getContentType("text/html;charset=utf-8"), ContentType.TYPE_HTML)
        assertEquals(ContentType.getFileNameContentType("audio/mp6"), ContentType.TYPE_OCTET_STREAM)
    }

    /**
     * 此方法用于自动生成`Content-Type`字段
     */
    @DisplayName("代码自动生成工具.")
    @Test
    @Disabled
    @SuppressWarnings("LongMethod", "NestedBlockDepth", "MagicNumber")
    fun createCode() {
        data class Content(val fileType: String, val application: String)

        val fromJson =
                Gson().fromJson(
                        javaClass.getResource("/res/contentType.json").openStream().readText(),
                        Array<Content>::class.javaObjectType
                )
        val printWriter = File(System.getProperty("user.home"), "/Desktop/a.txt").printWriter()

        val map = HashMap<String, String>()
        var ii: Int = 0
        for (contentType in fromJson) {

            var replace = contentType.application.substring(contentType.application.indexOf("/") + 1)
                    .replace(
                            "-",
                            "_"
                    ).replace(
                            ".",
                            "_"
                    ).toUpperCase()
                    .replace("_*", "").replace("*", "")
            if (replace.trim() == "") {
                replace = contentType.application.substring(0, contentType.application.indexOf("/")).toUpperCase()
            }
            val name = "TYPE_$replace"

            val element =
                    name + "(" +
                            "\"" + contentType.fileType + "\"," +
                            "\"" + contentType.application + "\"" +
                            ")"
            if (map.containsKey(name)) {
                for (i in 1..100) {
                    if (map.containsKey("${name}_$i")) {
                        continue
                    } else {
                        map["${name}_$i"] = element.replace(oldValue = name, newValue = "${name}_$i")
                        ii++
                        break
                    }
                }
            } else {
                map[name] = element
                ii++
            }
        }
        println("size:$ii")

        println("size:" + map.keys.size)
        println("size:" + map.values.size)
        val toMutableList = map.values.toMutableList()
        toMutableList.sort()
        for (key in toMutableList) {
            printWriter.println("$key,")
        }
        println("size:" + fromJson.size)
        println("size:" + toMutableList.size)
        printWriter.flush()
        printWriter.close()
    }
}
