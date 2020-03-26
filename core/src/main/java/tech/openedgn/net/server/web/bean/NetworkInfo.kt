package tech.openedgn.net.server.web.bean

import java.io.IOException
import java.net.Inet6Address
import java.net.InetAddress

/**
 * # 网络主机信息
 *
 * 此数据类用于记录网络主机信息，
 *
 * @property ip String ip地址
 * @property port Int 主机端口号
 * @property hostName String? 主机网络名称 （如果有）
 * @constructor @Throws(UnknownHostException::class)
 */

data class NetworkInfo(val ip: String, val port: Int, val hostName: String = ip) {
    companion object {
        const val MAX_PORT = 65535
        var PATTERN =
            "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$"
    }

    init {
        if ((InetAddress.getByName(ip) is Inet6Address).not() && ip.matches(Regex(PATTERN)).not()) {
            throw IOException("[$ip] 格式错误. (Unrecognized)")
        }
        // 是否为正确的 IP
        if (port < 0 || port > MAX_PORT) {
            throw IndexOutOfBoundsException("$port 不是正确的端口号.")
        }
    }

    /**
     * 判断是否为 IPv6 地址
     */
    val ipv6Host = InetAddress.getByName(ip) is Inet6Address

    override fun toString(): String {
        return "$ip:$port"
    }
}
