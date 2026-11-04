package com.seavpn.app
import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.content.Intent
import android.net.VpnService
class MainActivity: Activity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  window.statusBarColor=Color.rgb(0,105,105); window.navigationBarColor=Color.rgb(0,70,75)
  val layout=LinearLayout(this); layout.orientation=LinearLayout.VERTICAL; layout.gravity=Gravity.CENTER; layout.setPadding(32,32,32,32); layout.setBackgroundColor(Color.rgb(5,35,42))
  val logo=TextView(this); logo.text="✕"; logo.textSize=72f; logo.setTextColor(Color.WHITE); logo.gravity=Gravity.CENTER
  val title=TextView(this); title.text="SEA VPN"; title.textSize=30f; title.setTextColor(Color.WHITE); title.gravity=Gravity.CENTER
  val sub=TextView(this); sub.text="Secure • Simple • Global"; sub.setTextColor(Color.LTGRAY); sub.gravity=Gravity.CENTER
  val server=Spinner(this); server.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listOf("Global (demo)","Singapore (demo)","United States (demo)","Japan (demo)"))
  val btn=Button(this); btn.text="CONNECT"; btn.setOnClickListener { val i=VpnService.prepare(this); if(i!=null) startActivityForResult(i,10) else startService(Intent(this,SeaVpnService::class.java)); btn.text="DEMO — NOT CONNECTED" }
  layout.addView(logo); layout.addView(title); layout.addView(sub); layout.addView(server); layout.addView(btn); setContentView(layout)
 }
 override fun onActivityResult(r:Int,c:Int,d:Intent?){super.onActivityResult(r,c,d); if(r==10 && c==RESULT_OK) startService(Intent(this,SeaVpnService::class.java))}
}