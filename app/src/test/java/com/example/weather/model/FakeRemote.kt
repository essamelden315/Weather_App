package com.example.weather.model

import com.example.weather.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemote:RemoteSource {

    var data = MyResponse()
    override suspend fun getRetrofitList(
        lat: Double,
        lon: Double,
        exclude: String,
        lang: String,
        units: String
    ): Flow<MyResponse> {
        return flowOf(data)
    }
}