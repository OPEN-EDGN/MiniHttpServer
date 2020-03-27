package tech.openedgn.net.server.web

import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.request.HttpRequest
import tech.openedgn.net.server.web.request.reader.SimpleRequestReader
import tech.openedgn.net.server.web.response.Controller
import tech.openedgn.net.server.web.response.Get
import tech.openedgn.net.server.web.response.Post

fun main(args: Array<String>) {
    val webServer = WebServer(8088)
    val webConfig = webServer.webConfig
    webConfig.timeout = 3000
    WebServer.debug = true
    webServer.enable()
    webConfig.addController(Tests())

}

@Controller
public class Tests {
    @Get("/")
    public fun geta(bas: BaseHttpRequest): String {
        return "HelloWOrld"
    }
    @Post("/")
    public fun geta2(bas: BaseHttpRequest): String {
        return "HelloWOrld"
    }
}
