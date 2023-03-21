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

    override suspend fun getRetrofitList():MyResponse? {

        var response = myApiService.api_service.getWeatherDetails(33.44, -94.04,"minutely", lang = "ar","bbcb13e1d448621ffd8e565701972f6d")
        if (response.isSuccessful)
           result = response.body()!!
        return result
    }
}