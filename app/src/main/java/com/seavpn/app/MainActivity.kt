package com.seavpn.app
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.VpnService
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val vpnRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(6, 32, 36)
        window.navigationBarColor = Color.rgb(8, 42, 45)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 48, 28, 28)
            setBackgroundColor(Color.rgb(6, 28, 32))
        }

        val logo = TextView(this).apply {
            text = "SEA"
            textSize = 42f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 12)
        }

        val title = TextView(this).apply {
            text = "VPN"
            textSize = 28f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 18)
        }

        val subtitle = TextView(this).apply {
            text = "Masukkan server Hysteria 2 yang valid"
            textSize = 14f
            setTextColor(Color.argb(255, 200, 220, 220))
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 24)
        }

        val serverInput = EditText(this).apply {
            hint = "host:port (contoh: example.com:443)"
            setText("example.com:443")
            setTextColor(Color.WHITE)
            setHintTextColor(Color.argb(255, 180, 200, 200))
            setBackgroundColor(Color.argb(255, 18, 52, 58))
            setPadding(24, 20, 24, 20)
        }

        val authInput = EditText(this).apply {
            hint = "auth (opsional)"
            setText("")
            setTextColor(Color.WHITE)
            setHintTextColor(Color.argb(255, 180, 200, 200))
            setBackgroundColor(Color.argb(255, 18, 52, 58))
            setPadding(24, 20, 24, 20)
        }

        val sniInput = EditText(this).apply {
            hint = "SNI (opsional)"
            setText("example.com")
            setTextColor(Color.WHITE)
            setHintTextColor(Color.argb(255, 180, 200, 200))
            setBackgroundColor(Color.argb(255, 18, 52, 58))
            setPadding(24, 20, 24, 20)
        }

        val connectButton = Button(this).apply {
            text = "CONNECT"
            setBackgroundColor(Color.rgb(15, 112, 120))
            setTextColor(Color.WHITE)
            setOnClickListener {
                val server = serverInput.text?.toString()?.trim().orEmpty()
                val auth = authInput.text?.toString()?.trim().orEmpty()
                val sni = sniInput.text?.toString()?.trim().orEmpty()

                if (server.isEmpty()) {
                    serverInput.error = "Server wajib diisi"
                    return@setOnClickListener
                }

                val prepareIntent = VpnService.prepare(this@MainActivity)
                if (prepareIntent != null) {
                    startActivityForResult(prepareIntent, vpnRequestCode)
                } else {
                    startService(createServiceIntent(server, auth, sni))
                }
            }
        }

        val stopButton = Button(this).apply {
            text = "STOP"
            setBackgroundColor(Color.rgb(120, 52, 52))
            setTextColor(Color.WHITE)
            setOnClickListener {
                stopService(Intent(this@MainActivity, SeaVpnService::class.java))
            }
        }

        root.addView(logo)
        root.addView(title)
        root.addView(subtitle)
        root.addView(serverInput)
        root.addView(authInput)
        root.addView(sniInput)
        root.addView(connectButton)
        root.addView(stopButton)
        setContentView(root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == vpnRequestCode && resultCode == Activity.RESULT_OK) {
            val server = findViewById<EditText>(android.R.id.input).text?.toString() ?: ""
            val auth = findViewById<EditText>(android.R.id.input).text?.toString() ?: ""
            val sni = findViewById<EditText>(android.R.id.input).text?.toString() ?: ""
            startService(
                createServiceIntent(server, auth, sni)
            )
        }
    }

    private fun createServiceIntent(server: String, auth: String, sni: String): Intent {
        return Intent(this, SeaVpnService::class.java).apply {
            putExtra("server", server)
            putExtra("auth", auth)
            putExtra("sni", sni)
        }
    }
}
