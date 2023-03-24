package com.example.weather.network

import com.example.weather.model.MyResponse

interface RemoteSource {
    suspend fun getRetrofitList(lat:Double,lon:Double,exclude:String,lang:String,units:String):MyResponse?
}