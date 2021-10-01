package com.retrogeek46.wirelessinput

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket(serverIP:String) {
        try {
            Log.i("wirelessInput", "serverIP in SocketSingleton $serverIP")
            mSocket = IO.socket("http://192.168.1.9:3456")
//            mSocket = IO.socket(serverIP)
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }
}