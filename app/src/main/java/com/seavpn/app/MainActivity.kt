
package com.seavpn.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.drawable.GradientDrawable

class MainActivity : Activity() {

    private val bg = Color.rgb(7, 18, 29)
    private val panel = Color.rgb(17, 34, 48)
    private val teal = Color.rgb(0, 210, 190)
    private val white = Color.WHITE
    private val muted = Color.rgb(160, 180, 195)

    private lateinit var status: TextView
    private lateinit var connect: Button
    private lateinit var host: EditText
    private lateinit var port: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var sni: EditText
    private lateinit var mode: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(bg)

        val scroll = ScrollView(this)
        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        content.setPadding(24, 28, 24, 32)

        // HEADER
        val header = LinearLayout(this)
        header.orientation = LinearLayout.HORIZONTAL
        header.gravity = Gravity.CENTER_VERTICAL

        val logo = TextView(this)
        logo.text = "X"
        logo.textSize = 30f
        logo.setTextColor(Color.rgb(0, 120, 255))
        logo.gravity = Gravity.CENTER
        logo.typeface = Typeface.DEFAULT_BOLD
        logo.background = rounded(white, 18)
        header.addView(logo, LinearLayout.LayoutParams(60, 60))

        val brand = LinearLayout(this)
        brand.orientation = LinearLayout.VERTICAL
        brand.setPadding(16, 0, 0, 0)

        val title = TextView(this)
        title.text = "SEA VPN"
        title.textSize = 23f
        title.setTextColor(white)
        title.typeface = Typeface.DEFAULT_BOLD

        val subtitle = TextView(this)
        subtitle.text = "SECURE CONNECTION"
        subtitle.textSize = 10f
        subtitle.setTextColor(teal)

        brand.addView(title)
        brand.addView(subtitle)
        header.addView(brand)

        content.addView(header)
        gap(content, 28)

        // STATUS
        val statusCard = LinearLayout(this)
        statusCard.orientation = LinearLayout.VERTICAL
        statusCard.gravity = Gravity.CENTER
        statusCard.setPadding(20, 24, 20, 24)
        statusCard.background = rounded(panel, 22)

        val orb = TextView(this)
        orb.text = "●"
        orb.textSize = 35f
        orb.setTextColor(teal)
        orb.gravity = Gravity.CENTER

        status = TextView(this)
        status.text = "DISCONNECTED"
        status.textSize = 17f
        status.typeface = Typeface.DEFAULT_BOLD
        status.setTextColor(white)
        status.gravity = Gravity.CENTER

        val detail = TextView(this)
        detail.text = "Choose configuration to continue"
        detail.textSize = 12f
        detail.setTextColor(muted)
        detail.gravity = Gravity.CENTER

        statusCard.addView(orb)
        statusCard.addView(status)
        statusCard.addView(detail)
        content.addView(statusCard)
        gap(content, 22)

        // SERVER
        section(content, "SERVER CONFIGURATION")

        mode = Spinner(this)
        mode.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "SSH DIRECT",
                "SSH + TLS SNI",
                "SSH + WSS",
                "SSH + HTTP PAYLOAD"
            )
        )
        mode.background = rounded(panel, 12)
        content.addView(mode, matchWrap())
        gap(content, 12)

        host = field("SSH Host / IP")
        port = field("SSH Port (22)")
        username = field("SSH Username")
        password = field("SSH Password")
        password.inputType = 129

        sni = field("TLS SNI / WSS Host")

        content.addView(host, matchWrap())
        gap(content, 10)
        content.addView(port, matchWrap())
        gap(content, 10)
        content.addView(username, matchWrap())
        gap(content, 10)
        content.addView(password, matchWrap())
        gap(content, 10)
        content.addView(sni, matchWrap())
        gap(content, 18)

        // CONNECT
        connect = Button(this)
        connect.text = "CONNECT"
        connect.textSize = 16f
        connect.typeface = Typeface.DEFAULT_BOLD
        connect.setTextColor(Color.BLACK)
        connect.background = rounded(teal, 16)
        content.addView(connect, LinearLayout.LayoutParams(-1, 58))

        connect.setOnClickListener {
            status.text = "CONFIGURATION SAVED"
            Toast.makeText(
                this,
                "Pengaturan tersimpan. Tunnel VPN belum aktif.",
                Toast.LENGTH_LONG
            ).show()
        }

        gap(content, 18)

        val save = Button(this)
        save.text = "SAVE CONFIGURATION"
        save.setTextColor(white)
        save.background = rounded(panel, 14)
        content.addView(save, LinearLayout.LayoutParams(-1, 50))

        save.setOnClickListener {
            getSharedPreferences("sea_vpn", Context.MODE_PRIVATE)
                .edit()
                .putString("host", host.text.toString())
                .putString("port", port.text.toString())
                .putString("username", username.text.toString())
                .putString("sni", sni.text.toString())
                .putInt("mode", mode.selectedItemPosition)
                .apply()

            Toast.makeText(this, "Konfigurasi disimpan", Toast.LENGTH_SHORT).show()
        }

        gap(content, 24)

        val footer = TextView(this)
        footer.text = "SEA VPN • PRIVATE NETWORK\nVPN ENGINE NOT CONFIGURED"
        footer.textSize = 10f
        footer.setTextColor(muted)
        footer.gravity = Gravity.CENTER
        content.addView(footer)

        scroll.addView(content)
        root.addView(scroll)
        setContentView(root)
    }

    private fun section(parent: LinearLayout, text: String) {
        val tv = TextView(this)
        tv.text = text
        tv.textSize = 12f
        tv.typeface = Typeface.DEFAULT_BOLD
        tv.setTextColor(teal)
        parent.addView(tv)
        gap(parent, 10)
    }

    private fun field(hint: String): EditText {
        val e = EditText(this)
        e.hint = hint
        e.setHintTextColor(muted)
        e.setTextColor(white)
        e.textSize = 14f
        e.setSingleLine(true)
        e.setPadding(18, 12, 18, 12)
        e.background = rounded(panel, 12)
        return e
    }

    private fun rounded(color: Int, radius: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
        }
    }

    private fun gap(parent: LinearLayout, height: Int) {
        parent.addView(View(this), LinearLayout.LayoutParams(1, height))
    }

    private fun matchWrap(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(-1, 54)
    }
}
