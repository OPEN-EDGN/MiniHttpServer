package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.consts.METHOD
import java.lang.reflect.Field

/**
 *
 * @property replyMethod METHOD
 * @property replyHeader Map<String, String>
 * @property field Field
 * @property clazz Class<out Any>
 */
data class Controller(
    val replyMethod: METHOD,
    val replyHeader: Map<String, String>,
    val field: Field,
    val clazz: Class<out Any>
)
