package com.example.weather.model

import com.example.weather.database.LocalDataSource

interface RepositoryInterface :LocalDataSource{
    suspend fun getRetrofitWeatherData(lat:Double,lon:Double,exclude:String,lang:String,units:String):MyResponse?
}