package tech.openEdgn.netServer.webServer.io

import tech.openEdgn.netServer.webServer.bean.BaseDataReader
import java.io.InputStream

object Loder{




}


interface RequestBodyLoader {
    fun load(location: String, header: Map<String, String>, bufferedReader: InputStream,reader : HashMap<String, BaseDataReader>): Boolean
}