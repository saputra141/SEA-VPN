
package com.seavpn.app

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.drawable.GradientDrawable

class MainActivity : Activity() {

    private val bg = Color.rgb(7, 17, 27)
    private val card = Color.rgb(17, 35, 49)
    private val teal = Color.rgb(0, 210, 190)
    private val white = Color.WHITE
    private val muted = Color.rgb(150, 175, 190)

    private lateinit var status: TextView
    private lateinit var connect: Button
    private lateinit var server: Spinner
    private lateinit var sni: EditText

    private var connected = false

    companion object {
        private const val VPN_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUI()
    }

    private fun rounded(color: Int, radius: Int = 24): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
        }
    }

    private fun buildUI() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this)
        scroll.addView(root)

        val logo = TextView(this).apply {
            text = "X"
            textSize = 32f
            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(teal)
        }

        root.addView(logo, LinearLayout.LayoutParams(100, 100))

        val title = TextView(this).apply {
            text = "SEA VPN"
            textSize = 30f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(white)
        }

        root.addView(title)

        val subtitle = TextView(this).apply {
            text = "SECURE • FAST • PRIVATE"
            textSize = 12f
            gravity = Gravity.CENTER
            setTextColor(teal)
        }

        root.addView(subtitle)

        val statusCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = rounded(card, 28)
            setPadding(24, 36, 24, 36)
        }

        val dot = TextView(this).apply {
            text = "●"
            textSize = 36f
            gravity = Gravity.CENTER
            setTextColor(teal)
        }

        statusCard.addView(dot)

        status = TextView(this).apply {
            text = "NOT CONNECTED"
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(white)
        }

        statusCard.addView(status)

        val desc = TextView(this).apply {
            text = "Your connection is currently unprotected"
            textSize = 13f
            gravity = Gravity.CENTER
            setTextColor(muted)
        }

        statusCard.addView(desc)

        val params = LinearLayout.LayoutParams(
            -1, -2
        ).apply {
            topMargin = 40
            bottomMargin = 28
        }

        root.addView(statusCard, params)

        label(root, "SERVER LOCATION")

        server = Spinner(this)

        val locations = arrayOf(
            "Singapore 🇸🇬",
            "Indonesia 🇮🇩",
            "Japan 🇯🇵",
            "United States 🇺🇸"
        )

        server.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            locations
        )

        server.background = rounded(card, 18)

        root.addView(server, LinearLayout.LayoutParams(-1, 60))

        label(root, "TLS / SNI")

        sni = EditText(this).apply {
            hint = "SNI hostname"
            textSize = 15f
            setSingleLine(true)
            setTextColor(white)
            setHintTextColor(muted)
            background = rounded(card, 18)
            setPadding(20, 0, 20, 0)
        }

        root.addView(sni, LinearLayout.LayoutParams(-1, 60))

        connect = Button(this).apply {
            text = "CONNECT"
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(bg)
            background = rounded(teal, 24)
            setOnClickListener {
                if (!connected) requestVpn()
                else disconnectVpn()
            }
        }

        val connectParams = LinearLayout.LayoutParams(
            -1, 68
        ).apply {
            topMargin = 32
        }

        root.addView(connect, connectParams)

        val footer = TextView(this).apply {
            text = "\nSEA VPN • PRIVATE NETWORK\nUDP VPN ENGINE"
            textSize = 12f
            gravity = Gravity.CENTER
            setTextColor(muted)
        }

        root.addView(footer)

        setContentView(scroll)
    }

    private fun label(root: LinearLayout, text: String) {
        val view = TextView(this).apply {
            this.text = text
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(teal)
            setPadding(4, 24, 4, 10)
        }
        root.addView(view)
    }

    private fun requestVpn() {
        val intent = VpnService.prepare(this)

        if (intent != null) {
            startActivityForResult(intent, VPN_REQUEST)
        } else {
            startTunnel()
        }
    }

    @Deprecated("Legacy VPN permission callback")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VPN_REQUEST &&
            resultCode == RESULT_OK) {
            startTunnel()
        }
    }

    private fun startTunnel() {

        val intent = Intent(this, SeaVpnService::class.java)
        intent.action = SeaVpnService.ACTION_CONNECT

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        connected = true
        status.text = "VPN INTERFACE STARTED"
        connect.text = "DISCONNECT"
        connect.setTextColor(white)
        connect.background = rounded(Color.rgb(190, 50, 60), 24)

        Toast.makeText(
            this,
            "TUN interface started",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun disconnectVpn() {

        val intent = Intent(this, SeaVpnService::class.java)
        intent.action = SeaVpnService.ACTION_DISCONNECT
        startService(intent)

        connected = false
        status.text = "NOT CONNECTED"
        connect.text = "CONNECT"
        connect.setTextColor(bg)
        connect.background = rounded(teal, 24)
    }
}
