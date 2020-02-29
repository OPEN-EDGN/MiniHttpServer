package tech.openedgn.net.server.web.utils

import tech.openedgn.net.server.web.error.HttpException
import java.io.Closeable
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * 请不要多次使用同一runnable
 */
abstract class AutoCloseRunnable : Runnable {
    protected val logger = WebLogger(javaClass)
    private val autoCloseable = ConcurrentLinkedDeque<Closeable>()
    /**
     * 注册线程结束时自动销毁监听器
     */
    protected fun <T : Closeable> T.registerAutoClose(): T {
        autoCloseable.addFirst(this)
        return this
    }

    @SuppressWarnings("TooGenericExceptionCaught")
    override fun run() {
        logger.debug("线程实例 [${Thread.currentThread().name}] 已经启动.")
        try {
            execute()
        } catch (e: HttpException) {
            logger.warn(e.message)
        } catch (e: Exception) {
            logger.error("线程 [${Thread.currentThread().name}] 在执行过程中出现不可预测的异常.", e)
        } finally {
            for (close in autoCloseable) {
                try {
                    close.close()
                    logger.debug("执行[${close.javaClass.simpleName}]下Close()方法完成.")
                } catch (e: Exception) {
                    logger.error("执行[${close.javaClass.simpleName}]下Close()方法出现错误.", e)
                }
            }
        }
        autoCloseable.clear()
        logger.debug("如无意外，线程实例 [${Thread.currentThread().name}] 已经结束.")
    }

    /**
     * 此为 `Runnable#run()`的扩展
     *
     */
    @Throws(Exception::class)
    abstract fun execute()
}
