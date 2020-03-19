package tech.openedgn.net.server.web.response.controller

import java.io.Serializable

interface IMatcher : Serializable {
    fun matches(input: CharSequence): Boolean
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}