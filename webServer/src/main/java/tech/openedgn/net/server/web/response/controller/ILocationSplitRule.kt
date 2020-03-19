package tech.openedgn.net.server.web.response.controller


interface ILocationSplitRule {
    /**
     * 客户端请求 Location 字段切割规则
     */
    fun acceptLocationSplit(location: String): Array<String>
    /**
     *  Controller 绑定的 Location 字段切割规则
     */
    fun bindLocationSplit(location: String): Array<IMatcher>

//    /**
//     * 创建匹配此字段的规则
//     *
//     * @param bindLocationSplitItem String 此字段与 `#bindLocationSplit(String):Array<String>` 方法字段匹配
//     * @return 能匹配的规则
//     */
//    fun splitDecode(bindLocationSplitItem: String): IMatcher
}