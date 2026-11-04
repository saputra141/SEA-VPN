
package com.seavpn.app

data class VpnConfig(
    val serverName: String = "Custom server",
    val serverAddress: String,
    val serverPort: Int = 443,
    val password: String,
    val sni: String,
    val insecure: Boolean = false
)

object SeaVpnConfig {
    var current: VpnConfig? = null
}
