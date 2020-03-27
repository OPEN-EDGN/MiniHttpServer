package tech.openedgn.net.server.web.response.matcher

class StringMatcher(private val regex: String) :
    IMatcher {
    override fun matches(input: CharSequence): Boolean {
        return regex == input
    }
    override fun equals(other: Any?): Boolean {
        return if (other != null && other is StringMatcher) {
            return regex == other.regex
        } else {
            false
        }
    }
    override fun hashCode(): Int {
        return regex.hashCode()
    }
}