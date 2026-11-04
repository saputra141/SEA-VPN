package com.seavpn.app

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val backgroundColor = Color.rgb(7, 24, 32)
    private val surfaceColor = Color.rgb(16, 39, 48)
    private val mutedColor = Color.rgb(157, 179, 185)
    private val accentColor = Color.rgb(77, 220, 190)
    private var selectedLocation = "Singapura · Demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = backgroundColor
        window.navigationBarColor = backgroundColor
        window.decorView.systemUiVisibility = 0

        selectedLocation = getPreferences(MODE_PRIVATE)
            .getString(KEY_LOCATION, selectedLocation) ?: selectedLocation

        val scrollView = ScrollView(this).apply {
            setBackgroundColor(backgroundColor)
            isFillViewport = true
        }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(28), dp(24), dp(28))
        }

        content.addView(buildHeader())
        content.addView(space(34))
        content.addView(label("JARINGAN PRIBADI", accentColor, 12f, true))
        content.addView(space(10))
        content.addView(label("Internet lebih\nnyaman dan aman.", Color.WHITE, 32f, true))
        content.addView(space(10))
        content.addView(label(
            "Kelola pilihan server dan status koneksi SEA VPN dalam satu tempat.",
            mutedColor,
            15f,
            false,
        ))
        content.addView(space(26))
        content.addView(buildStatusCard())
        content.addView(space(16))
        content.addView(buildLocationCard())
        content.addView(space(16))
        content.addView(buildInfoButton())
        content.addView(space(26))
        content.addView(buildInfoRow())
        content.addView(space(26))
        content.addView(label(
            "Mode demo: aplikasi ini belum memiliki server atau tunnel VPN aktif. Pilihan lokasi hanya contoh.",
            mutedColor,
            12f,
            false,
        ).apply { gravity = Gravity.CENTER; textAlignment = View.TEXT_ALIGNMENT_CENTER })

        scrollView.addView(content)
        setContentView(scrollView)
    }

    private fun buildHeader(): View = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        val mark = label("S", Color.rgb(7, 24, 32), 22f, true).apply {
            gravity = Gravity.CENTER
            background = rounded(accentColor, 16f)
        }
        addView(mark, LinearLayout.LayoutParams(dp(48), dp(48)))

        val brand = LinearLayout(this@MainActivity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), 0, 0, 0)
            addView(label("SEA VPN", Color.WHITE, 17f, true))
            addView(label("PRIVATE NETWORK", mutedColor, 10f, true))
        }
        addView(brand, LinearLayout.LayoutParams(0, -2, 1f))
        addView(label("DEMO", accentColor, 11f, true).apply {
            gravity = Gravity.CENTER
            setPadding(dp(12), dp(8), dp(12), dp(8))
            background = rounded(Color.rgb(23, 62, 62), 50f)
        })
    }

    private fun buildStatusCard(): View = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(20), dp(20), dp(20), dp(20))
        background = rounded(surfaceColor, 24f)

        addView(label("●  STATUS KONEKSI", accentColor, 12f, true))
        addView(space(16))
        addView(label("Belum terhubung", Color.WHITE, 22f, true))
        addView(space(6))
        addView(label("Belum ada tunnel VPN yang berjalan.", mutedColor, 14f, false))
        addView(space(18))
        addView(View(this@MainActivity).apply {
            setBackgroundColor(Color.rgb(37, 62, 69))
        }, LinearLayout.LayoutParams(-1, dp(1)))
        addView(space(14))
        addView(label("Perlindungan VPN  ·  Tidak aktif", mutedColor, 13f, false))
    }

    private fun buildLocationCard(): View = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(20), dp(18), dp(20), dp(18))
        background = rounded(surfaceColor, 22f)
        isClickable = true
        isFocusable = true
        contentDescription = "Pilih lokasi demo, saat ini $selectedLocation"
        setOnClickListener { showLocationPicker() }

        addView(label("LOKASI SERVER", mutedColor, 11f, true))
        addView(space(10))
        val row = LinearLayout(this@MainActivity).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(label("◎", accentColor, 25f, true))
            addView(label(selectedLocation, Color.WHITE, 16f, true).apply {
                setPadding(dp(12), 0, 0, 0)
            }, LinearLayout.LayoutParams(0, -2, 1f))
            addView(label("⌄", mutedColor, 22f, false))
        }
        addView(row)
        addView(space(8))
        addView(label("Pilihan lokasi hanya untuk pratinjau.", mutedColor, 12f, false))
    }

    private fun buildInfoButton(): View = label("Tentang koneksi VPN", backgroundColor, 15f, true).apply {
        gravity = Gravity.CENTER
        setPadding(dp(18), dp(17), dp(18), dp(17))
        background = rounded(accentColor, 18f)
        isClickable = true
        isFocusable = true
        setOnClickListener { showSetupNotice() }
    }

    private fun buildInfoRow(): View = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        addView(buildInfoItem("KONEKSI", "Belum aktif"), LinearLayout.LayoutParams(0, -2, 1f))
        addView(spaceWidth(12))
        addView(buildInfoItem("SERVER", "Mode demo"), LinearLayout.LayoutParams(0, -2, 1f))
    }

    private fun buildInfoItem(title: String, value: String): View = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(15), dp(12), dp(15))
        background = rounded(surfaceColor, 18f)
        addView(label(title, mutedColor, 10f, true))
        addView(space(8))
        addView(label(value, Color.WHITE, 14f, true))
    }

    private fun showLocationPicker() {
        val locations = arrayOf("Singapura · Demo", "Jepang · Demo", "Amerika Serikat · Demo")
        AlertDialog.Builder(this)
            .setTitle("Pilih lokasi demo")
            .setSingleChoiceItems(locations, locations.indexOf(selectedLocation)) { dialog, index ->
                selectedLocation = locations[index]
                getPreferences(MODE_PRIVATE).edit().putString(KEY_LOCATION, selectedLocation).apply()
                recreate()
                dialog.dismiss()
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    private fun showSetupNotice() {
        AlertDialog.Builder(this)
            .setTitle("Koneksi VPN belum tersedia")
            .setMessage(
                "SEA VPN saat ini hanya menyediakan tampilan demo. Agar koneksi sungguhan berfungsi, " +
                    "aplikasi memerlukan konfigurasi server dan implementasi tunnel yang valid. " +
                    "Jangan menganggap perangkat terlindungi saat ini.",
            )
            .setPositiveButton("Mengerti", null)
            .show()
    }

    private fun label(text: String, color: Int, sizeSp: Float, bold: Boolean): TextView =
        TextView(this).apply {
            this.text = text
            setTextColor(color)
            textSize = sizeSp
            if (bold) setTypeface(typeface, Typeface.BOLD)
            includeFontPadding = true
        }

    private fun rounded(color: Int, radiusDp: Float): GradientDrawable = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radiusDp.toInt()).toFloat()
    }

    private fun space(heightDp: Int): View = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(1, dp(heightDp))
    }

    private fun spaceWidth(widthDp: Int): View = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(dp(widthDp), 1)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    companion object {
        private const val KEY_LOCATION = "selected_location"
    }
}
