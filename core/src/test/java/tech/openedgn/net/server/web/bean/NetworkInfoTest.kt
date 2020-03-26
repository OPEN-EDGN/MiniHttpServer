package tech.openedgn.net.server.web.bean

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class NetworkInfoTest {
    companion object {
        const val CUSTOM_PORT = 1000
        const val IPV4_ADDRESS = "127.0.0.1"
        const val UNKNOWN_IP_ADDRESS = "UNKNOWN_IP_ADDRESS"
        const val IPV6_ADDRESS_A = "fe80::483:77ff:feb3:4d63"
        const val IPV6_ADDRESS_B = "[::]"
    }

    private val ipv4Info = NetworkInfo(IPV4_ADDRESS, CUSTOM_PORT)
    private val ipv6InfoA = NetworkInfo(IPV6_ADDRESS_A, CUSTOM_PORT)
    private val ipv6InfoB = NetworkInfo(IPV6_ADDRESS_B, CUSTOM_PORT)

    @Test
    fun initTest() {
         assertThrows<IOException>("抛出未知IP错误") {
            NetworkInfo(UNKNOWN_IP_ADDRESS, CUSTOM_PORT)
        }
    }

    @Test
    fun ipv6HostTest() {
        assertFalse(ipv4Info.ipv6Host)
        assertTrue(ipv6InfoA.ipv6Host)
        assertTrue(ipv6InfoB.ipv6Host)
    }
}
