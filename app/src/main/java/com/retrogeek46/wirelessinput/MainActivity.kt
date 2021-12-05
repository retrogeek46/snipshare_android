package com.retrogeek46.wirelessinput

import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.EditText
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private var debugTag = "wirelessInput"
    private var mGlobalSocket: io.socket.client.Socket? = null
    private var serverIP = ""
    lateinit var serverIPEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        serverIPEditText = findViewById(R.id.serverIPInput)
//        serverIP = getServerIP()
//        serverIP = "http://192.168.1.3:3456";
        Log.i(debugTag, "the serverIP is $serverIP")
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