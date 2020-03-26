package tech.openedgn.net.server.web.utils

import java.io.Closeable
import java.io.IOException
import java.lang.Exception


fun Closeable?.safeCloseIt(logger: WebLogger){
    if (this != null){
        try {
            close()
        }catch (e:IOException){
            logger.debug("安全关闭时出现IO错误.",e)
        }catch (e:Exception){
            logger.debug("安全关闭时出现未知错误.",e)
        }
    }}
fun Closeable?.safeCloseIt(tag:String = ""){
    if (this != null){
        try {
            close()
        }catch (e:IOException){
            getWebLogger(this.javaClass,tag).debug("安全关闭时出现IO错误.",e)
        }catch (e:Exception){
            getWebLogger(this.javaClass,tag).debug("安全关闭时出现未知错误.",e)
        }
    }
}