package com.example.weather.model

import com.example.weather.database.LocalDataSource
import com.example.weather.network.RemoteSource

class Repository private constructor(rs: RemoteSource,ls:LocalDataSource):RepositoryInterface {
    var remoteSource:RemoteSource = rs
    var localSource:LocalDataSource = ls

    companion object {
        private var myInstance: Repository? = null
        fun getInstance(remote:RemoteSource,local:LocalDataSource): Repository? {
            if (myInstance==null)
                myInstance = Repository(remote,local)
            return myInstance
        }
    }
    override suspend fun getRetrofitWeatherData(lat:Double,lon:Double,exclude:String,lang:String,units:String): MyResponse? {
        return remoteSource.getRetrofitList(lat,lon,exclude,lang,units)
    }

    override suspend fun showFavData(): List<SavedDataFormula>? {
        return localSource.showFavData()
    }

    override suspend fun insertFavData(savedDataFormula: SavedDataFormula) {
       localSource.insertFavData(savedDataFormula)
    }

    override suspend fun deleteFromFav(savedDataFormula: SavedDataFormula) {
        localSource.deleteFromFav(savedDataFormula)
    }
}