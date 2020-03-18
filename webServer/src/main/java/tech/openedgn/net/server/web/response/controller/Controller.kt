package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.consts.METHOD
import java.lang.reflect.Field

/**
 *
 */
data class Controller(val field:Field,val method: METHOD,val className:Class<out Any>,val regexLocationName:Array<String>) {



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Controller

        if (field != other.field) return false
        if (method != other.method) return false
        if (className != other.className) return false
        if (!regexLocationName.contentEquals(other.regexLocationName)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + className.hashCode()
        result = 31 * result + regexLocationName.contentHashCode()
        return result
    }

}