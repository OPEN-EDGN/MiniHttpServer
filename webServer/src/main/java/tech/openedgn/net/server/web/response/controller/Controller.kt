package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.consts.METHOD
import java.lang.reflect.Field


data class Controller(
    val replyMethod: (METHOD) -> Boolean,
    val replyHeader: Array<Pair<String, String>>,
    val field: Field,
    val clazz: Class<out Any>
) {
    /**
     *  IDEA auto created.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Controller

        if (replyMethod != other.replyMethod) return false
        if (!replyHeader.contentEquals(other.replyHeader)) return false
        if (field != other.field) return false
        if (clazz != other.clazz) return false

        return true
    }

    /**
     *  IDEA auto created.
     */
    override fun hashCode(): Int {
        var result = replyMethod.hashCode()
        result = 31 * result + replyHeader.contentHashCode()
        result = 31 * result + field.hashCode()
        result = 31 * result + clazz.hashCode()
        return result
    }
}
