package tech.openedgn.net.server.web

import org.junit.jupiter.api.Test
import tech.openEdgn.tools4k.safeClose
import java.io.Closeable

class WebServerTest {

    @Test
    fun test() {
            val c: Closeable? = null
        c.safeClose()
//        val file = File("D:/Env/test.txt")
//        val inputStream = file.inputStream()
//        val bufferedReader = BufferedReader(inputStream.reader(),1)
//        bufferedReader.reset()
//        for (i in 0 until 3) {
//            println(bufferedReader.readLine())
//        }
//        val byteArray = ByteArray(10)
//        println(inputStream.available())
//        inputStream.read(byteArray)
//        println(byteArray.toString(Charsets.UTF_8))
    }
}
