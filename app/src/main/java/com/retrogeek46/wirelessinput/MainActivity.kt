package com.retrogeek46.wirelessinput

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mGlobalSocket: io.socket.client.Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyA = findViewById<Button>(R.id.key_A)
        val keyB = findViewById<Button>(R.id.key_B)
        val keyC = findViewById<Button>(R.id.key33)

        SocketHandler.setSocket()
        var mSocket = SocketHandler.getSocket()
        mSocket.connect()

        mGlobalSocket = mSocket

//        keyA.setOnClickListener {
//            mSocket.emit("fromAndroid", "keyA")
//        }
    }

    fun sendMessage(view: View) {
        mGlobalSocket?.emit("fromAndroid", view.tag.toString())
    }
}