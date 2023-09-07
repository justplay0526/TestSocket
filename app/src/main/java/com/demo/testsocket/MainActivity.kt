package com.demo.testsocket

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var testView: TextView
    private lateinit var mainHandler: Handler

    var lat = 25.04409250082649
    var lon = 121.53439899652307

    private val updateTextTask = object : Runnable {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.S)
        override fun run() {
            lat += 0.001
            lon += 0.001
            mainHandler.postDelayed(this, 1000)
            testView.text = "latitude:$lat\nlongitude:$lon"
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testView = findViewById(R.id.testView)

        // 取得位置管理器
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mainHandler = Handler(Looper.getMainLooper())
// 建立一个模擬位置
        val mockLocation = Location(LocationManager.GPS_PROVIDER)
        mockLocation.latitude = lat  // 設定緯度
        mockLocation.longitude = lon  // 設定經度
        mockLocation.accuracy = 1f  // 設定精度
        mockLocation.time = System.currentTimeMillis() //設定時間戳記
        mockLocation.elapsedRealtimeNanos= SystemClock.elapsedRealtimeNanos()

// 模擬位置更新
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, true, false,
            false, false, true, true, true, ProviderProperties.POWER_USAGE_MEDIUM,ProviderProperties.ACCURACY_COARSE)
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation)

//        // 停止模擬位置
//        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, false)
    }
    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }
}