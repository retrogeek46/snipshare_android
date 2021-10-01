package com.retrogeek46.wirelessinput

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyA = findViewById<Button>(R.id.key_A)
        val keyB = findViewById<Button>(R.id.key_B)
        val keyC = findViewById<Button>(R.id.key_C)

        SocketHandler.setSocket()
        val mSocket = SocketHandler.getSocket()
        mSocket.connect()

//        fun keyA(view:View) {
//            mSocket.emit("fromAndroid")
//        }

        keyA.setOnClickListener {
            mSocket.emit("fromAndroid")
        }
    }
}