package tech.openedgn.net.server.web.utils.dataBlock

import tech.openEdgn.tools4k.METHOD
import tech.openEdgn.tools4k.calculatedHash
import tech.openedgn.net.server.web.ClosedException
import tech.openedgn.net.server.web.utils.ClosedManager
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * 文件类型的数据块
 * (会自动删除)
 * @property blockFile File 数据块文件
 */
class TempFileDataBlock(
    private val blockFile: File
) : BaseFileDataBlock(blockFile, true) {
}