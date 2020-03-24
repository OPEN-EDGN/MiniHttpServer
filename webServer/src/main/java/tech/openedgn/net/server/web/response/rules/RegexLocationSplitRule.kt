package tech.openedgn.net.server.web.response.rules

import tech.openedgn.net.server.web.response.matcher.IMatcher
import tech.openedgn.net.server.web.utils.getWebLogger
import java.util.*

class RegexLocationSplitRule : ILocationSplitRule {
    private val logger = getWebLogger()
    override fun acceptLocationSplit(location: String): Array<String> {
        val linkedList = LinkedList<String>()
        linkedList.addAll(location.split(Regex("/")))
        linkedList.removeFirst()
        if (location.endsWith("/")){
            linkedList.removeLast()
            linkedList.addLast("/")
        }
        return linkedList.toArray(arrayOf())
    }

    override fun bindLocationSplit(location: String): Array<IMatcher> {
        TODO()
    }




}