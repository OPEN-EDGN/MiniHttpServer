package tech.openedgn.net.server.web.utils

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class BufferedInputStreamTest {
    lateinit var data: String
    @BeforeAll
    fun before() {
        data = "LINE A\r\r" +
                "LINE B\r\n" +
                "LINE C\r" +
                "LINE D\r\n"
    }

    val logger = getWebLogger()
    @Test
    fun test() {
        val inputStream = data.byteInputStream(Charsets.ISO_8859_1)
        val dataReader = BufferedInputStream(inputStream)

        var line: String? = ""
        while (line != null) {
            line = dataReader.readLine(true)
            logger.info("->$line<-")
        }
    }
}
