package tech.openedgn.net.server.web.utils

import tech.openEdgn.tools4k.toPrintString
import java.text.SimpleDateFormat
import java.util.Locale

fun Any.getWebLogger() = WebLogger(this.javaClass)
fun Any.getWebLogger(any: Any) = WebLogger(any.javaClass)

enum class WebLoggerLevel(val levelInt: Int) {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3);
}

data class WebOutputItem(
    val clazz: Class<out Any>,
    val remoteAddress: String,
    val threadName: String,
    val level: WebLoggerLevel,
    val message: String,
    val exception: Throwable?
)

class WebLogger constructor(private val clazz: Class<out Any>) {

    var remoteAddress: String = "HOST"

    /**
     */
    fun debugOnly(t: (WebLogger) -> Unit): WebLogger {
        if (isDebug) {
            t(this)
        }
        return this
    }

    @JvmOverloads
    fun info(message: String, exception: Throwable? = null): WebLogger {
        outputLogger(Thread.currentThread().name,
                WebLoggerLevel.INFO, message, exception)
        return this
    }

    @JvmOverloads
    fun debug(message: String, exception: Throwable? = null): WebLogger {
        outputLogger(Thread.currentThread().name,
                WebLoggerLevel.DEBUG, message, exception)
        return this
    }

    @JvmOverloads
    fun warn(message: String, exception: Throwable? = null): WebLogger {
        outputLogger(Thread.currentThread().name,
                WebLoggerLevel.WARN, message, exception)
        return this
    }

    @JvmOverloads
    fun error(message: String, exception: Throwable? = null): WebLogger {
        outputLogger(Thread.currentThread().name,
                WebLoggerLevel.ERROR, message, exception)
        return this
    }

    private fun outputLogger(threadName: String, level: WebLoggerLevel, message: String, exception: Throwable?) {
        WebLoggerConfig.outputLog(
                WebOutputItem(
                        clazz,
                        remoteAddress,
                        threadName.toUpperCase(Locale.ENGLISH),
                        level,
                        message,
                        exception
                )
        )
    }

    val isDebug: Boolean
        get() = WebLoggerConfig.debug
}

object WebLoggerConfig {

    private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    var loggerOutputHook: (WebOutputItem) -> Boolean = { false }

    fun outputLog(webOutputItem: WebOutputItem) {
        if (webOutputItem.level == WebLoggerLevel.DEBUG && debug.not()) {
            return
        }
        if (loggerOutputHook(webOutputItem).not()) {
            val stringBuilder = StringBuilder()
            stringBuilder.append(simpleDateFormat.format(System.currentTimeMillis()))
                    .append(" - ")
                    .append(String.format("%-5s", webOutputItem.level.name))
                    .append(" - ")
                    .append(webOutputItem.remoteAddress)
                    .append(" - ")
                    .append(webOutputItem.clazz.simpleName)
                    .append(":")
                    .append(webOutputItem.message.replace(Regex("\\p{C}"), "#"))
            if (webOutputItem.exception != null) {
                stringBuilder.append("\r\n")
                        .append(webOutputItem.exception.toPrintString())
            }
            println(stringBuilder)
        }
    }

    var debug: Boolean = false
}
