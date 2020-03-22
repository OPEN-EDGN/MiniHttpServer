package tech.openedgn.net.server.web.response.responseLoader

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.bean.NetworkInfo
import tech.openedgn.net.server.web.request.BaseHttpRequest
import tech.openedgn.net.server.web.response.BaseHttpResponse
import java.io.Closeable

abstract class BaseResponseControllerLoader(
    protected val networkInfo: NetworkInfo,
    protected val webConfig: WebConfig
) : Closeable {
    /**
     * 是否存在对应的解析器
     *
     *  根据请求信息判断是否存在对应的解析器
     *
     * @param request 请求信息
     * @return Boolean true or false
     */
    abstract fun responseExists(request: BaseHttpRequest): Boolean

    /**
     * 写入解析器信息到容器
     *
     * @param httpResponse BaseHttpResponse 容器
     */
    abstract fun loadResponse(httpResponse: BaseHttpResponse)
}