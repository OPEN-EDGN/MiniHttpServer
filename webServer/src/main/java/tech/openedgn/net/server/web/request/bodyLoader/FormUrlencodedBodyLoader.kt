package tech.openedgn.net.server.web.request.bodyLoader

import tech.openedgn.net.server.web.request.FormItem
import tech.openedgn.net.server.web.utils.DecodeUtils
import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import tech.openedgn.net.server.web.utils.WebLogger

class FormUrlencodedBodyLoader (logger: WebLogger) : BaseBodyLoader(logger) {
    override fun load(
        location: String,
        header: Map<String, String>,
        dataBlock: IDataBlock,
        forms: HashMap<String, FormItem>
    ): Boolean {
        DecodeUtils.decodeFormData(dataBlock.toString(Charsets.ISO_8859_1),forms,logger)
        return true
    }

}