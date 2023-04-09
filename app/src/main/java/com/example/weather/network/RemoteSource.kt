package com.example.weather.network

import com.example.weather.model.MyResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {
     fun getRetrofitList(lat:Double,lon:Double,exclude:String,lang:String,units:String): Flow<Response<MyResponse>>
}