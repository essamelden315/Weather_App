package com.example.weather.network

import com.example.weather.model.MyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api_Service {
    @GET("onecall")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("lang") lang:String = "ar",
        @Query("appid") appid: String
    ): Response<MyResponse>
}