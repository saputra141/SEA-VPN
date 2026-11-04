
package com.seavpn.app

import android.net.VpnService
import android.content.Intent
import android.os.IBinder
import android.util.Log

class SeaVpnService : VpnService() {

    companion object {
        const val ACTION_CONNECT =
            "com.seavpn.app.CONNECT"

        const val ACTION_DISCONNECT =
            "com.seavpn.app.DISCONNECT"

        const val EXTRA_SERVER =
            "server_address"

        const val EXTRA_PORT =
            "server_port"

        const val EXTRA_PASSWORD =
            "server_password"

        const val EXTRA_SNI =
            "server_sni"
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {

            ACTION_CONNECT -> {
                val address =
                    intent.getStringExtra(EXTRA_SERVER) ?: ""

                val port =
                    intent.getIntExtra(EXTRA_PORT, 443)

                val password =
                    intent.getStringExtra(EXTRA_PASSWORD) ?: ""

                val sni =
                    intent.getStringExtra(EXTRA_SNI) ?: ""

                if (
                    address.isBlank() ||
                    password.isBlank() ||
                    sni.isBlank()
                ) {
                    Log.e("SEA VPN", "Config tidak lengkap")
                    stopSelf()
                    return START_NOT_STICKY
                }

                val config = VpnConfig(
                    serverAddress = address,
                    serverPort = port,
                    password = password,
                    sni = sni
                )

                SeaVpnConfig.current = config

                Log.i(
                    "SEA VPN",
                    "Config diterima: $address:$port"
                )

                // Native Hysteria 2 belum terintegrasi.
                Log.e(
                    "SEA VPN",
                    "Hysteria engine belum tersedia"
                )

                stopSelf()
            }

            ACTION_DISCONNECT -> {
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}
