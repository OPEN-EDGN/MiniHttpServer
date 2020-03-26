package tech.openedgn.net.server.web.response.rules

import tech.openedgn.net.server.web.response.matcher.IMatcher


interface ILocationSplitRule {
    /**
     * 客户端请求 Location 字段切割规则
     */
    fun acceptLocationSplit(location: String): Array<String>
    /**
     *  Controller 绑定的 Location 字段切割规则
     */
    fun bindLocationSplit(location: String): Array<IMatcher>

}