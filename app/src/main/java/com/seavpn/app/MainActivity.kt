
package com.seavpn.app

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
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
    private lateinit var serverSpinner: Spinner
    private lateinit var sniInput: EditText

    private var permissionGranted = false

    companion object {
        private const val VPN_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
    }

    private fun shape(
        color: Int,
        radius: Int = 22
    ) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius.toFloat()
    }

    private fun createUI() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this)
        scroll.addView(root)

        // HEADER
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

        val name = TextView(this).apply {
            text = "SEA VPN"
            textSize = 23f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(white)
        }

        val tagline = TextView(this).apply {
            text = "SECURE CONNECTION"
            textSize = 11f
            setTextColor(accent)
        }

        brand.addView(name)
        brand.addView(tagline)
        header.addView(brand)

        root.addView(header)

        // STATUS CARD
        val statusCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = shape(panel, 26)
            setPadding(20, 32, 20, 32)
        }

        val statusIcon = TextView(this).apply {
            text = "●"
            textSize = 40f
            gravity = Gravity.CENTER
            setTextColor(accent)
        }

        statusCard.addView(statusIcon)

        status = TextView(this).apply {
            text = "NOT CONNECTED"
            textSize = 21f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(white)
        }

        statusCard.addView(status)

        detail = TextView(this).apply {
            text = "Your connection is not protected"
            textSize = 13f
            gravity = Gravity.CENTER
            setTextColor(muted)
        }

        statusCard.addView(detail)

        val statusParams = LinearLayout.LayoutParams(-1, -2)
        statusParams.topMargin = 32
        statusParams.bottomMargin = 26
        root.addView(statusCard, statusParams)

        // SERVER
        section(root, "SERVER LOCATION")

        serverSpinner = Spinner(this)

        val servers = arrayOf(
            "Singapore 🇸🇬",
            "Indonesia 🇮🇩",
            "Japan 🇯🇵",
            "United States 🇺🇸"
        )

        serverSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            servers
        )

        serverSpinner.background = shape(panel, 16)

        root.addView(
            serverSpinner,
            LinearLayout.LayoutParams(-1, 58)
        )

        // SNI
        section(root, "TLS / SNI")

        sniInput = EditText(this).apply {
            hint = "Masukkan hostname SNI"
            textSize = 15f
            setSingleLine(true)
            setTextColor(white)
            setHintTextColor(muted)
            background = shape(panel, 16)
            setPadding(20, 0, 20, 0)
        }

        root.addView(
            sniInput,
            LinearLayout.LayoutParams(-1, 58)
        )

        // CONNECT
        connectButton = TextView(this).apply {
            text = "CONNECT"
            textSize = 19f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setTextColor(bg)
            background = shape(accent, 25)
            elevation = 8f

            setOnClickListener {
                requestPermission()
            }
        }

        val buttonParams = LinearLayout.LayoutParams(-1, 68)
        buttonParams.topMargin = 30
        root.addView(connectButton, buttonParams)

        // FOOTER
        val footer = TextView(this).apply {
            text = "\nSEA VPN • PRIVATE NETWORK\nUDP / TLS VPN"
            textSize = 12f
            gravity = Gravity.CENTER
            setTextColor(muted)
        }

        root.addView(footer)

        setContentView(scroll)
    }

    private fun section(
        root: LinearLayout,
        text: String
    ) {
        val label = TextView(this).apply {
            this.text = text
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(accent)
            setPadding(2, 18, 2, 10)
        }

        root.addView(label)
    }

    private fun requestPermission() {

        val intent = VpnService.prepare(this)

        if (intent != null) {
            startActivityForResult(
                intent,
                VPN_REQUEST
            )
        } else {
            permissionGranted = true
            showEngineNotice()
        }
    }

    @Deprecated("Legacy VPN permission callback")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == VPN_REQUEST &&
            resultCode == RESULT_OK
        ) {
            permissionGranted = true
            showEngineNotice()
        }
    }

    private fun showEngineNotice() {

        status.text = "ENGINE NOT CONFIGURED"
        detail.text =
            "Izin VPN diberikan • Server belum terhubung"

        connectButton.text = "ENGINE NOT READY"

        connectButton.setTextColor(white)
        connectButton.background =
            shape(Color.rgb(65, 78, 90), 25)

        Toast.makeText(
            this,
            "Izin VPN berhasil. Engine UDP belum dipasang.",
            Toast.LENGTH_LONG
        ).show()
    }
}
