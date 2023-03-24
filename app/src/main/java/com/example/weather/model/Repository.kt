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
    override suspend fun getRetrofitWeatherData(lat:Double,lon:Double,exclude:String,lang:String,units:String): MyResponse? {
        return remoteSource.getRetrofitList(lat,lon,exclude,lang,units)
    }
}