package com.example.weather.model

interface RepositoryInterface {
    suspend fun getRetrofitWeatherData(lat:Double,lon:Double,exclude:String,lang:String,units:String):MyResponse?
}