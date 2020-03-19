package tech.openedgn.net.server.web

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.openedgn.net.server.web.response.controller.RegexLocationSplitRule
import tech.openedgn.net.server.web.response.controller.RegexMatcher
import java.io.Closeable
import java.util.*

class WebServerTest {

    @Test
    fun test() {
        val regexLocationSplitRule = RegexLocationSplitRule()
        println(regexLocationSplitRule.acceptLocationSplit("/asas/asasa/").toList().toString())
    }
}
