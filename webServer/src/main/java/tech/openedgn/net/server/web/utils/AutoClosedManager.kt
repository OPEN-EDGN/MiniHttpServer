package tech.openedgn.net.server.web.utils

import tech.openedgn.net.server.web.error.ClosedException
import java.io.Closeable

abstract class AutoClosedManager(tag:String = "") :Closeable{
    private val privateLogger =WebLogger(AutoClosedManager::class.java)
    init {
        if (tag.isEmpty()){
            privateLogger.remoteAddress = javaClass.simpleName
        }else{
            privateLogger.remoteAddress = tag

        }
    }
    /**
     * 自动销毁模块寄存
     */
    protected val closeMap = LinkedHashSet<Closeable>()
    /**
     * 此对象是否关闭
     */
    @Volatile
    protected var closeable = false

    protected fun <T : Closeable> T.registerCloseable(): T {
        if (closeable) {
            throw ClosedException("此对象生命周期已经结束！")
        }
        synchronized(closeMap) {
            closeMap.add(this)
            privateLogger.debug("对象[${javaClass.simpleName}]，已被注册 ")
        }
        return this
    }
    @Synchronized
    override fun close() {
        closeable = true
        closeMap.forEach {
            try {
                it.close()
                privateLogger.debug("执行[${it.javaClass.simpleName}]下Close()方法完成.")
            } catch (e: Exception) {
                privateLogger.error("执行[${it.javaClass.simpleName}]下Close()方法出现错误.", e)
            }
        } // 销毁对象
        closeMap.clear()
        privateLogger.debug("如无意外，对象 [${javaClass.simpleName}] 已经结束.")

    }
}