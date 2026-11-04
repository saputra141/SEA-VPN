package com.seavpn.app
import android.net.VpnService
import android.content.Intent
class SeaVpnService: VpnService() {
 override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
  // Starter placeholder only: no tunnel/server configured; this does NOT provide internet VPN.
  stopSelf(); return START_NOT_STICKY
 }
 override fun onDestroy(){ super.onDestroy() }
}