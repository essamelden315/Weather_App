package com.example.weather.model

import com.example.weather.network.RemoteSource

class Repository private constructor(rs: RemoteSource):RepositoryInterface {
    var remoteSource:RemoteSource = rs

    companion object {
        private var myInstance: Repository? = null
        fun getInstance(rmtsorc:RemoteSource): Repository? {
            if (myInstance==null)
                myInstance = Repository(rmtsorc)
            return myInstance
        }
    }
    override suspend fun getRetrofitWeatherData(): MyResponse? {
        return remoteSource.getRetrofitList()
    }
}