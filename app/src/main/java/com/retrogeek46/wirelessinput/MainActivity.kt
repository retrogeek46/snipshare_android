package com.retrogeek46.wirelessinput

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var debugTag = "wirelessInput"
    private var mGlobalSocket: io.socket.client.Socket? = null
    private var serverIP = ""
    lateinit var touchpad: View
    lateinit var serverIPEditText: EditText


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        serverIPEditText = findViewById(R.id.serverIPInput)
        touchpad = findViewById(R.id.touchpad)
//        serverIP = getServerIP()
//        serverIP = "http://192.168.1.7:3456";
        Log.i(debugTag, "the serverIP is $serverIP")

        touchpad.setOnTouchListener{ _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    Log.i(debugTag, "current pos X: ${motionEvent.x} Y: ${motionEvent.y}")
                    mGlobalSocket?.emit("draw", motionEvent.x.toString() + "|" + motionEvent.y.toString())
                }
            }
            true
        }
    }

    fun startServer(view: View) {
//        Log.i(debugTag, view.tag.toString())

        SocketHandler.setSocket(serverIPEditText.text.toString())
        mGlobalSocket = SocketHandler.getSocket()
        mGlobalSocket?.connect()
//        mGlobalSocket?.emit("fromAndroid", view.tag.toString())
    }

    fun sendMessage(view: View) {
        mGlobalSocket?.emit("fromAndroid", view.tag.toString())
    }

    private fun getServerIP(): String {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

        val ipPrefix:String = ipAddress.subSequence(0,ipAddress.lastIndexOf(".")).toString()

        var urlResponse = ""
        var serverIp = ""
        var i = 0

        while(serverIp == "" && i < 250) thread {
            val url = "http://$ipPrefix.$i:3456/connect"

            urlResponse = try {
                URL(url).readText()
            } catch (e: Throwable) {
                ""
            }
            if (urlResponse == "hi") {
                serverIp = "http://$ipPrefix.$i:3456"
            }
            i++
        }
        return serverIp
    }
}