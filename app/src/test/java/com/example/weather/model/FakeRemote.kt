package com.example.weather.model

import com.example.weather.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class FakeRemote:RemoteSource {

    var data = MyResponse()
    override  fun getRetrofitList(
        lat: Double,
        lon: Double,
        exclude: String,
        lang: String,
        units: String
    ): Flow<Response<MyResponse>> {
        return flow{data}
    }
}