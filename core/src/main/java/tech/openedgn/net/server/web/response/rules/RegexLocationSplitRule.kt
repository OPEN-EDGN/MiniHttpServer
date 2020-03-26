package tech.openedgn.net.server.web.response.rules

import tech.openedgn.net.server.web.response.matcher.IMatcher
import tech.openedgn.net.server.web.response.matcher.RegexMatcher
import tech.openedgn.net.server.web.utils.getWebLogger
import java.util.*

class RegexLocationSplitRule : ILocationSplitRule {
    private val logger = getWebLogger()
    override fun acceptLocationSplit(location: String): Array<String> {
        val linkedList = LinkedList<String>()
        val locationUp = location.toUpperCase()
        linkedList.addAll(locationUp.split(Regex("/")))
        linkedList.removeFirst()
        if (locationUp.endsWith("/")){
            linkedList.removeLast()
            linkedList.addLast("/")
        }
        return linkedList.toArray(arrayOf())
    }

    override fun bindLocationSplit(location: String): Array<IMatcher> {
        val locationUp = location.toUpperCase()
        val list = locationUp.split(Regex("/"))
        val linkedList = LinkedList<IMatcher>()
        for (s in list) {
            linkedList.add(RegexMatcher(Regex("^$s$")))
        }
        return linkedList.toArray(arrayOf())
    }




}