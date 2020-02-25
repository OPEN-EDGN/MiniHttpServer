package tech.openEdgn.netServer.webServer.utils

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DataReaderTest {
    lateinit var data: String
    @Before
    fun before() {
        data = "LINE A\r\r" +
                "LINE B\r\n" +
                "LINE C\r" +
                "LINE D\r\n"


    }

    val logger = getWebLogger()
    @Test
    fun test(): Unit {
        val inputStream = data.byteInputStream(Charsets.ISO_8859_1)
        val dataReader = DataReader(inputStream)

        var line: String? = ""
        while (line != null) {
            line = dataReader.readLine(true)
            logger.info("->$line<-")
        }
    }


}