package tech.openedgn.net.server.web.data

import com.google.gson.Gson
import tech.openEdgn.tools4k.readText
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class ContentTypeTest {
    data class Content(val fileType: String, val application: String)

    fun test() {
        val fromJson =
            Gson().fromJson(
                javaClass.getResource("/res/application.json").openStream().readText(),
                Array<Content>::class.javaObjectType
            )
        val printWriter = File("/home/dragon/Desktop/a.txt").printWriter()

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
