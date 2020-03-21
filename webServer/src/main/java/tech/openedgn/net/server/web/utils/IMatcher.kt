package tech.openedgn.net.server.web.utils

import java.io.Serializable

/**
 *  字符串匹配工具
 */
interface IMatcher : Serializable {
    /**
     * 判断字符串是否匹配规则，如果匹配则返回 TRUE
     */
    fun matches(input: CharSequence): Boolean
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}