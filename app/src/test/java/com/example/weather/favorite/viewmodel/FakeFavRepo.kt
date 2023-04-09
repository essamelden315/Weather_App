package com.example.weather.favorite.viewmodel

import com.example.weather.model.AlertData
import com.example.weather.model.MyResponse
import com.example.weather.model.RepositoryInterface
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeFavRepo:RepositoryInterface {
    var favourites :MutableList<SavedDataFormula> = mutableListOf()
    var alerts : MutableList<AlertData> = mutableListOf()

    override fun getHomeData(): Flow<MyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHomeData(myResponse: MyResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHomeData() {
        TODO("Not yet implemented")
    }

    override fun showFavData(): Flow<List<SavedDataFormula>> = flow {
        emit(favourites.toList())
    }

    override suspend fun insertFavData(savedDataFormula: SavedDataFormula) {
        favourites.add(savedDataFormula)
    }

    override suspend fun deleteFromFav(savedDataFormula: SavedDataFormula) {
        favourites.remove(savedDataFormula)
    }

    override fun getAlertData(): Flow<List<AlertData>> = flow {
        emit(alerts.toList())
    }

    override suspend fun insertIntoAlertTable(alertData: AlertData) {
        alerts.add(alertData)
    }

    override suspend fun deletefromAlertTable(alertData: AlertData) {
       alerts.remove(alertData)
    }

    override fun getRetrofitList(
        lat: Double,
        lon: Double,
        exclude: String,
        lang: String,
        units: String
    ): Flow<Response<MyResponse>> {
        TODO("Not yet implemented")
    }
}