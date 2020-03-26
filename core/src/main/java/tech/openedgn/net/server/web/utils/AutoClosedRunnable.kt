package tech.openedgn.net.server.web.utils

import tech.openedgn.net.server.web.HttpException
import tech.openedgn.net.server.web.WebServerInternalException
import java.io.Closeable

/**
 * 请不要多次使用同一runnable
 */
abstract class AutoClosedRunnable(tag:String = "") : Runnable,Closeable {
    protected val logger = getWebLogger(javaClass,tag)

    @SuppressWarnings("TooGenericExceptionCaught")
    override fun run() {
        logger.debug("线程实例 [${Thread.currentThread().name}] 已经启动.")
        try {
            execute()
        } catch (e: WebServerInternalException) {
            logger.error("内部错误!")
            logger.error(e.mess, e.throwable)
        } catch (e: HttpException) {
            logger.warn(e.message)
        } catch (e: Exception) {
            logger.error("线程 [${Thread.currentThread().name}] 在执行过程中出现不可预测的异常.", e)
        } finally {
            safeCloseIt(logger)
        }
        logger.debug("如无意外，线程实例 [${Thread.currentThread().name}] 已经结束.")
    }

    /**
     * 此为 `Runnable#run()`的扩展
     *
     */
    @Throws(Exception::class)
    abstract fun execute()
}
