
package com.seavpn.app

/**
 * SEA VPN Hysteria 2 engine bridge.
 *
 * Native Hysteria core must be integrated before
 * this bridge can establish a real VPN connection.
 */
class HysteriaEngine {

    private var running = false

    fun isRunning(): Boolean = running

    fun start(config: VpnConfig) {
        require(config.serverAddress.isNotBlank()) {
            "Server address belum diisi"
        }

        require(config.password.isNotBlank()) {
            "Password Hysteria belum diisi"
        }

        require(config.sni.isNotBlank()) {
            "TLS SNI belum diisi"
        }

        throw UnsupportedOperationException(
            "Native Hysteria core belum terintegrasi"
        )
    }

    fun stop() {
        running = false
    }
}
