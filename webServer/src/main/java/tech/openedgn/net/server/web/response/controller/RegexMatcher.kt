package tech.openedgn.net.server.web.response.controller

class RegexMatcher(private val regex: Regex) : IMatcher {
    override fun matches(input: CharSequence): Boolean {
        return regex.matches(input)
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is RegexMatcher) {
            return regex.pattern.equals(other.regex.pattern)
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return regex.pattern.hashCode()
    }
}