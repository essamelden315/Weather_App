package com.example.weather.network

import android.content.Context
import android.net.ConnectivityManager

class NetworkListener {
    companion object {
        fun getConnectivity(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null
        }
    }
}