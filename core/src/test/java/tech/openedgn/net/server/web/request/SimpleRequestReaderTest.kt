package tech.openedgn.net.server.web.request

import org.junit.jupiter.api.Test

class SimpleRequestReaderTest{
    @Test
    fun test(): Unit {
        println("/get?adasd=31231#asasa".split(Regex("#"), 2)[0])
        println("/get?adasd=31231#asasa".split("?"))


    }
}