package com.retrogeek46.wirelessinput

import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.text.format.Formatter
import android.text.format.Formatter.formatIpAddress
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var mGlobalSocket: io.socket.client.Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()
        mSocket.connect()

        mGlobalSocket = mSocket

        getServerIP()

//        keyA.setOnClickListener {
//            mSocket.emit("fromAndroid", "keyA")
//        }

//        Log.i("wirelessInput",ipAddress)
    }

    fun sendMessage(view: View) {
        mGlobalSocket?.emit("fromAndroid", view.tag.toString())
    }

    fun getServerIP() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

        var ipPrefix:String = ipAddress.subSequence(0,ipAddress.lastIndexOf(".")).toString()

        Log.i("wirelessInput", ipPrefix)

        var serverIp:String = ""

        val queue = Volley.newRequestQueue(this)
        for (i in 0..250) {
            val url = "$ipPrefix.$i"
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    Log.i("wirelessInput","Response is: ${response.substring(0, 500)} for $url")
                    serverIp = url
                },
                { Log.i("wirelessInput","That didn't work for $url") })
            queue.add(stringRequest)
        }
    }
}