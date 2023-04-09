package com.example.weather.model

import com.example.weather.database.LocalDataSource
import com.example.weather.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

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
   override fun getRetrofitList(lat:Double, lon:Double, exclude:String, lang:String, units:String): Flow<Response<MyResponse>>{
      return remoteSource.getRetrofitList(lat,lon,exclude,lang,units)
   }
    override fun getHomeData(): Flow<MyResponse> {
        return localSource.getHomeData()
    }

    override suspend fun insertHomeData(myResponse: MyResponse) {
        localSource.insertHomeData(myResponse)
    }

    override suspend fun deleteHomeData() {
        localSource.deleteHomeData()
    }

    override fun showFavData(): Flow<List<SavedDataFormula>> {
        return localSource.showFavData()
    }


    override suspend fun insertFavData(savedDataFormula: SavedDataFormula) {
       localSource.insertFavData(savedDataFormula)
    }

    override suspend fun deleteFromFav(savedDataFormula: SavedDataFormula) {
        localSource.deleteFromFav(savedDataFormula)
    }

    override fun getAlertData(): Flow<List<AlertData>> {
        return localSource.getAlertData()
    }

    override suspend fun insertIntoAlertTable(alertData: AlertData) {
        localSource.insertIntoAlertTable(alertData)
    }

    override suspend fun deletefromAlertTable(alertData: AlertData) {
        localSource.deletefromAlertTable(alertData)
    }


}