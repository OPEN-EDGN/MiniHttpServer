package tech.openEdgn.netServer.webServer.bean
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
 * @constructor
 */
data class NetworkInfo (val ip:String,val port:Int,val hostName:String=ip){


    /**
     *  IPv6
     */
    val isIpv6Host
    get() = InetAddress.getByName(ip) is Inet6Address


    override fun toString(): String {
        return "$ip:$port"
    }

}