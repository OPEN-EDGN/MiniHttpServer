package tech.openedgn.net.server.web.utils

import tech.openedgn.net.server.web.ClosedException
import java.io.Closeable

/**
 * 为对象提供一种自动执行 #Close方法的方案
 *
 * （ 极易造成内存泄漏！！！ 非必要场景请勿使用！！！）
 *
 * @property closeable Boolean
 */
interface IClosedManager{
    /**
     * 此对象是否关闭
     */
    var closeable :Boolean

    /**
     * 为 实现  `Closeable` 接口的对象添加自动销毁
     *
     */
    fun <T : Closeable> T.registerCloseable(): T


    /**
     * 取消注册
     */
    fun <T : Closeable> T.unRegisterCloseable(): T

    /**
     * 销毁所有
     */
    fun closeAllRegisterCloseable()
}

abstract class ClosedManager(tag:String = "",private val disableLogger:Boolean = false) :IClosedManager{
    private val privateLogger =WebLogger(javaClass)
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
    private val closeMap = LinkedHashSet<Closeable>()

    @Volatile
    override var closeable = false

    override fun <T : Closeable> T.registerCloseable(): T {
        if (closeable) {
            throw ClosedException("此对象生命周期已经结束！")
        }
        synchronized(closeMap) {
            closeMap.add(this)
            if (!disableLogger){
                privateLogger.debug("对象[${javaClass.simpleName}]，已注册自动关闭. ")
            }
        }
        return this
    }
    override fun <T : Closeable> T.unRegisterCloseable(): T {
        if (closeable) {
            throw ClosedException("此对象生命周期已经结束！")
        }
        synchronized(closeMap) {
            if (closeMap.remove(this) && !disableLogger ) {
                privateLogger.debug("对象[${javaClass.simpleName}]，已取消注册自动关闭.")
            }
        }
        return this
    }

    /**
     * 此方法最后被调用
     */
    open fun closeIt() {

    }

    /**
     * 执行此方法来关闭所有
     */
    @Synchronized
    override fun closeAllRegisterCloseable() {
        closeable = true
        closeMap.forEach {
            try {
                it.close()
                if (!disableLogger){
                    privateLogger.debug("执行[${it.javaClass.simpleName}]下Close()方法完成.")
                }
                closeIt()
            } catch (e: Exception) {
                if (!disableLogger) {
                    privateLogger.error("执行[${it.javaClass.simpleName}]下Close()方法出现错误.", e)
                }
            }
        } // 销毁对象
        closeMap.clear()
        if (!disableLogger) {
            privateLogger.debug("如无意外，对象 [${javaClass.simpleName}] 生命周期已经结束.")
        }
    }
}