package com.example.weather.network


import com.example.weather.model.MyResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductClient: RemoteSource {
lateinit var result:MyResponse

    companion object {
        private var myInstance: ProductClient? = null
        fun getInstance(): ProductClient? {
            if (myInstance==null)
                myInstance = ProductClient()
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

    override suspend fun getRetrofitList(lat:Double,lon:Double,exclude:String,lang:String,units:String):MyResponse? {

        var response = myApiService.api_service.getWeatherDetails(lat,lon,exclude,lang,units)
        if (response.isSuccessful)
           result = response.body()!!
        return result
    }
}