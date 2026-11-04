
package com.seavpn.app

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    private val bg = Color.rgb(6, 15, 25)
    private val panel = Color.rgb(17, 34, 48)
    private val accent = Color.rgb(0, 215, 190)
    private val blue = Color.rgb(20, 105, 235)
    private val white = Color.WHITE
    private val muted = Color.rgb(155, 178, 192)

    private lateinit var status: TextView
    private lateinit var detail: TextView
    private lateinit var connectButton: TextView
    private lateinit var address: EditText
    private lateinit var port: EditText
    private lateinit var password: EditText
    private lateinit var sni: EditText

    companion object {
        const val VPN_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
    }

    private fun shape(color: Int, radius: Int = 20) =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
        }

    private fun field(hintText: String, secret: Boolean = false) =
        EditText(this).apply {
            hint = hintText
            textSize = 15f
            setSingleLine(true)
            setTextColor(white)
            setHintTextColor(muted)
            background = shape(panel)
            setPadding(20, 0, 20, 0)

            if (secret) {
                inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

    private fun createUI() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 30)
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this)
        scroll.addView(root)

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val logo = TextView(this).apply {
            text = "X"
            textSize = 26f
            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(blue)
            background = shape(white, 16)
        }

        header.addView(logo, LinearLayout.LayoutParams(52, 52))

        val brand = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(14, 0, 0, 0)
        }

        brand.addView(TextView(this).apply {
            text = "SEA VPN"
            textSize = 23f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(white)
        })

        brand.addView(TextView(this).apply {
            text = "SECURE CONNECTION"
            textSize = 11f
            setTextColor(accent)
        })

        header.addView(brand)
        root.addView(header)

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = shape(panel, 25)
            setPadding(20, 28, 20, 28)
        }

        card.addView(TextView(this).apply {
            text = "●"
            textSize = 36f
            gravity = Gravity.CENTER
            setTextColor(accent)
        })

        status = TextView(this).apply {
            text = "NOT CONNECTED"
            textSize = 21f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(white)
        }

        detail = TextView(this).apply {
            text = "VPN belum aktif"
            textSize = 13f
            gravity = Gravity.CENTER
            setTextColor(muted)
        }

        card.addView(status)
        card.addView(detail)

        val cp = LinearLayout.LayoutParams(-1, -2)
        cp.topMargin = 28
        cp.bottomMargin = 20
        root.addView(card, cp)

        section(root, "SERVER ADDRESS")
        address = field("IP atau domain server")
        root.addView(address, fieldParams())

        section(root, "UDP PORT")
        port = field("443")
        port.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        root.addView(port, fieldParams())

        section(root, "HYSTERIA 2 PASSWORD")
        password = field("Password server", true)
        root.addView(password, fieldParams())

        section(root, "TLS / SNI")
        sni = field("Hostname TLS")
        root.addView(sni, fieldParams())

        connectButton = TextView(this).apply {
            text = "CONNECT"
            textSize = 19f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(bg)
            background = shape(accent, 25)
            elevation = 8f
            setOnClickListener { connectVPN() }
        }

        val bp = LinearLayout.LayoutParams(-1, 66)
        bp.topMargin = 26
        root.addView(connectButton, bp)

        val disconnect = TextView(this).apply {
            text = "DISCONNECT"
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(white)
            background = shape(Color.rgb(65, 78, 90), 25)
            setOnClickListener {
                startService(
                    Intent(this@MainActivity, SeaVpnService::class.java)
                        .setAction(SeaVpnService.ACTION_DISCONNECT)
                )
                status.text = "NOT CONNECTED"
                detail.text = "VPN dihentikan"
            }
        }

        val dp = LinearLayout.LayoutParams(-1, 58)
        dp.topMargin = 12
        root.addView(disconnect, dp)

        root.addView(TextView(this).apply {
            text = "\nSEA VPN • HYSTERIA 2 / UDP\nNative engine belum tersedia"
            textSize = 12f
            gravity = Gravity.CENTER
            setTextColor(muted)
        })

        setContentView(scroll)
    }

    private fun fieldParams() =
        LinearLayout.LayoutParams(-1, 56)

    private fun section(root: LinearLayout, label: String) {
        root.addView(TextView(this).apply {
            text = label
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(accent)
            setPadding(2, 16, 2, 9)
        })
    }

    private fun connectVPN() {
        val server = address.text.toString().trim()
        val serverPort = port.text.toString().toIntOrNull()
        val pass = password.text.toString()
        val hostname = sni.text.toString().trim()

        if (server.isEmpty() || serverPort == null ||
            serverPort !in 1..65535 || pass.isEmpty() ||
            hostname.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Lengkapi alamat, port, password, dan SNI",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val permission = VpnService.prepare(this)

        if (permission != null) {
            startActivityForResult(permission, VPN_REQUEST)
        } else {
            sendConfigToService()
        }
    }

    @Deprecated("Legacy VPN permission callback")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VPN_REQUEST) {
            if (resultCode == RESULT_OK) {
                sendConfigToService()
            } else {
                Toast.makeText(
                    this,
                    "Izin VPN ditolak",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendConfigToService() {
        val intent = Intent(this, SeaVpnService::class.java).apply {
            action = SeaVpnService.ACTION_CONNECT
            putExtra(SeaVpnService.EXTRA_SERVER,
                address.text.toString().trim())
            putExtra(SeaVpnService.EXTRA_PORT,
                port.text.toString().toInt())
            putExtra(SeaVpnService.EXTRA_PASSWORD,
                password.text.toString())
            putExtra(SeaVpnService.EXTRA_SNI,
                sni.text.toString().trim())
        }

        startService(intent)

        status.text = "ENGINE NOT READY"
        detail.text = "Konfigurasi dikirim • Hysteria core belum ada"

        Toast.makeText(
            this,
            "Konfigurasi diteruskan ke VPN service",
            Toast.LENGTH_LONG
        ).show()
    }
}
