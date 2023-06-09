package com.example.weather.network


import com.example.weather.model.MyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClient: RemoteSource {
lateinit var result:MyResponse

    companion object {
        private var myInstance: WeatherClient? = null
        fun getInstance(): WeatherClient? {
            if (myInstance==null)
                myInstance = WeatherClient()
            return myInstance
        }
    }
    object myRetrofit {
        val retrofit: Retrofit =
            Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }

    object myApiService {
        val api_service: Api_Service by lazy {
            myRetrofit.retrofit.create(Api_Service::class.java)
        }
    }

    override fun getRetrofitList(lat:Double,lon:Double,exclude:String,lang:String,units:String): Flow<Response<MyResponse>> {
        return flow{ emit(myApiService.api_service.getWeatherDetails(lat,lon,exclude,lang,units)) }
    }
}