package tech.openedgn.net.server.web.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RequestReaderImplTest{
    @Test
    fun test(): Unit {
        println("/get?adasd=31231#asasa".split(Regex("#"), 2)[0])
        println("/get?adasd=31231#asasa".split("?"))


    }
}