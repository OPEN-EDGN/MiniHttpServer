package tech.openedgn.net.server.web.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import java.io.File

class IDataBlockTest {

    @TempDir
    lateinit var tempDir: File

    private val dataA by lazy {
        ByteArrayDataBlock("HELLOWORLD".toByteArray(Charsets.US_ASCII))
    }
    private val dataB by lazy {
        val file = File(tempDir, "data.tmp")
        file.writeText("HelloWorld", Charsets.US_ASCII)
        FileDataBlock(file)
    }
    private val dataC by lazy {
        val file = File(tempDir, "data2.tmp")
        file.writeText("HelloWorld", Charsets.US_ASCII)
        FileDataBlock(file, true)
    }

    @Test
    fun getSize() {
        assertEquals(dataA.size, "HELLOWORLD".toByteArray(Charsets.US_ASCII).size.toLong())
        assertEquals(dataB.size, File(tempDir, "data.tmp").length())
        assertEquals(dataC.size, File(tempDir, "data2.tmp").length())
    }
    private val dataD by lazy {
        val file = File(tempDir, "data3.tmp")
        file.writeText("HelloWorld", Charsets.US_ASCII)
        FileDataBlock(file, true)
    }

    @Test
    fun get() {
        assertEquals(dataA[0].toChar(), 'H')
        assertEquals(dataB[0].toChar(), 'H')
    }

    @Test
    fun close() {
        dataC.openInputStream()
        dataC.openInputStream()
        dataC.close()
        assertTrue(File(tempDir, "data2.tmp").isFile.not())
    }

    @Test
    fun testToString() {
        assertEquals(dataA.toString(Charsets.US_ASCII), "HELLOWORLD")
        assertEquals(dataB.toString(Charsets.US_ASCII), "HelloWorld")
    }
}
