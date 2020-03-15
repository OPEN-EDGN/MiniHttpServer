package tech.openedgn.net.server.web.request.bodyLoader

import tech.openedgn.net.server.web.bean.FormItem
import tech.openedgn.net.server.web.utils.DataBlockOutputStream
import tech.openedgn.net.server.web.utils.DecodeUtils
import tech.openedgn.net.server.web.utils.IDataBlock
import tech.openedgn.net.server.web.utils.WebLogger

class FormUrlencodedBodyLoader (logger: WebLogger) : BaseBodyLoader(logger) {
    override fun load(
        location: String,
        header: Map<String, String>,
        dataBlock: IDataBlock,
        forms: HashMap<String, FormItem>,
        tempDataBlockConstructorFun: (name: String) -> DataBlockOutputStream
    ): Boolean {
        DecodeUtils.decodeFormData(dataBlock.toString(Charsets.ISO_8859_1),forms,logger)
        return true
    }

}