package tech.openedgn.net.server.web

fun main(args: Array<String>) {
    val webServer = WebServer(8088)
    val webConfig = webServer.webConfig
    webConfig.timeout = 3000
    WebServer.debug = true
    webServer.enable()
}
