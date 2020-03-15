package tech.openedgn.net.server.web.utils.dataBlock


// fun File.createDataReader() = FileDataBlock(this)

fun String.createDataReader() =
    ByteArrayDataBlock(this.toByteArray(Charsets.UTF_8))
