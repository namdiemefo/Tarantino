package com.naemo.tarantino.util


import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class NetworkUtil {

    private val url: String = "https://www.google.com"


    init {

    }

    fun isNetworkConnected(): Boolean {
        val value = false
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Test")
            connection.setRequestProperty("Connection", "close")
            connection.connectTimeout = 1000
            connection.connect()
            Log.d("Network Utils", "hasInternetConnected: ${(connection.responseCode == 200)}")
            val value = (connection.responseCode == 200)
            return value
        } catch (e: IOException) {
            Log.e("Network Utils", "Error checking internet connection", e)
        }

        Log.d("Network Utils", "hasInternetConnected: false")
        return false

    }

}
