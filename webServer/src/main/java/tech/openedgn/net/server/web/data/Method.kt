package tech.openedgn.net.server.web.data

enum class METHOD(val text: String) {
    GET("GET"),
    POST("POST"),
}

data class MethodData(val type: METHOD, val location: String, val httpVersion: String = "HTTP/1.1") {
    override fun toString(): String {
        return "${type.text} $location $httpVersion"
    }
}
