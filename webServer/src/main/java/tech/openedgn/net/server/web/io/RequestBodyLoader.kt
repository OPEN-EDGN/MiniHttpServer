package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.utils.BaseDataReader
import java.io.InputStream

object Loder

interface RequestBodyLoader {
    fun load(
        location: String,
        header: Map<String, String>,
        bufferedReader: InputStream,
        reader: HashMap<String, BaseDataReader>
    ): Boolean
}
