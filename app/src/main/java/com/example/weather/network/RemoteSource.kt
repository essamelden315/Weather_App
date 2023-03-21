package com.example.weather.network

import com.example.weather.model.MyResponse

interface RemoteSource {
    suspend fun getRetrofitList():MyResponse?
}