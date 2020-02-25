package tech.openEdgn.netServer.webServer.io

import tech.openEdgn.netServer.webServer.bean.BaseFormData
import tech.openEdgn.netServer.webServer.utils.DataReader
import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStream

object Loder{




}


interface RequestBodyLoader {
    fun load(location: String, header: Map<String, String>, bufferedReader: InputStream,reader : HashMap<String, BaseFormData>): Boolean
}



