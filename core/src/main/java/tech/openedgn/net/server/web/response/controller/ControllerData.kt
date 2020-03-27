package tech.openedgn.net.server.web.response.controller

import java.lang.reflect.Method

data class ControllerData (
    val className:String,
    val method: Method
)