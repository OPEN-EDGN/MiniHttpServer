package tech.openedgn.net.server.web.io

import tech.openedgn.net.server.web.utils.BaseDataReader
import java.io.Closeable

/**
 *  此方法用于读取POST表单
 */
interface RequestBodyLoader : Closeable {
    /**
     * 读取表单的数据方法
     *
     * 此方法用于读取POST的表单数据
     *
     * @param location String
     * @param header Map<String, String>
     * @param dataReader BaseDataReader
     * @param reader HashMap<String, BaseDataReader>
     * @return Boolean
     */
    fun load(
        location: String,
        header: Map<String, String>,
        dataReader: BaseDataReader,
        reader: HashMap<String, BaseDataReader>
    ): Boolean
}

class FormDataBodyLoader : RequestBodyLoader {

    override fun load(
        location: String,
        header: Map<String, String>,
        dataReader: BaseDataReader,
        reader: HashMap<String, BaseDataReader>
    ): Boolean {

        return true
    }

    override fun close() {

    }
}
