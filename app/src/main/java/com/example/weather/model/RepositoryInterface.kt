package com.example.weather.model

interface RepositoryInterface {
    suspend fun getRetrofitWeatherData():MyResponse?
}