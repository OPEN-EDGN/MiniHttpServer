package tech.openedgn.net.server.web.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertLinesMatch
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.lang.StringBuilder

@SuppressWarnings("MagicNumber")
class BufferedInputStreamTest {
    private val testTextA = arrayListOf("Test A", "Test B", "Test C", "Test D")
    private val testTextAByteArrayA = testTextA.let {
        val s = StringBuilder()
        it.forEach { data -> s.append(data).append("\r\n") }
        s.toString().toByteArray(Charsets.ISO_8859_1)
    }

    @DisplayName("测试 readLineEndWithCRLF(true) 方法")
    @Test
    fun readLineEndWithCRLFTestA() {
        val bufferedInputStream = BufferedInputStream(testTextAByteArrayA.inputStream())
        var read: String? = ""
        val list = mutableListOf<String>()
        while (read != null) {
            read = bufferedInputStream.readLineEndWithCRLF(true)
            if (read != null) {
                list.add(read)
            }
        }
        for ((i, v) in list.withIndex()) {
            assertEquals(v, "${testTextA[i]}\r")
        }
    }

    @DisplayName("测试 readLineEndWithCRLF(false) 方法")
    @Test
    fun readLineEndWithCRLFTestB() {
        val bufferedInputStream = BufferedInputStream(testTextAByteArrayA.inputStream())
        var read: String? = ""
        val list = mutableListOf<String>()
        while (read != null) {
            read = bufferedInputStream.readLineEndWithCRLF(false)
            if (read != null) {
                list.add(read)
            }
        }
        assertLinesMatch(list, testTextA)
    }

    @DisplayName("测试 readLine(true) 方法")
    @Test
    fun readLineTestA() {
        val bufferedInputStream = BufferedInputStream(testTextAByteArrayA.inputStream())
        var read: String? = ""
        val list = mutableListOf<String>()
        while (read != null) {
            read = bufferedInputStream.readLine(true)
            if (read != null) {
                list.add(read)
            }
        }
        assertLinesMatch(list, testTextA)
    }

    @DisplayName("测试 readLine(false) 方法")
    @Test
    fun readLineTestB() {
        val bufferedInputStream = BufferedInputStream(testTextAByteArrayA.inputStream())
        var read: String? = ""
        val list = mutableListOf<String>()
        while (read != null) {
            read = bufferedInputStream.readLine(false)
            if (read != null) {
                list.add(read)
            }
        }
        assertEquals(list[0], testTextA[0])
        assertEquals(list[2], testTextA[1])
        assertEquals(list[4], testTextA[2])
        assertEquals(list[6], testTextA[3])
    }
}
