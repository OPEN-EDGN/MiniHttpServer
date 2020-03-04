package tech.openedgn.net.server.web.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DataBlockOutputStreamTest {
    @TempDir
    lateinit var tempDir: File

    private val data by lazy {
        DataBlockOutputStream(File(tempDir, "data.temp"))
    }

    @BeforeEach
    fun init() {
        val b = "HELLOWORLD".toByteArray(Charsets.US_ASCII)
        for (i in 1..100) {
            data.write(b)
        }
    }
    @Test
    fun openDataReader() {
        val openDataReader = data.openDataReader()
        assertEquals(openDataReader.size, 100 * 10)
    }

    @Test
    fun close() {
        data.openDataReader().close()
        assertTrue(File(tempDir, "data.temp").isFile.not())
    }
}
