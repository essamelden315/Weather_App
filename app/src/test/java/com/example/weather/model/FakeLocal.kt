package com.example.weather.model

import com.example.weather.database.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocal:LocalDataSource {
    var location :MutableList<SavedDataFormula> = mutableListOf()

    override fun getHomeData(): Flow<MyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHomeData(myResponse: MyResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHomeData() {
        TODO("Not yet implemented")
    }

    override fun showFavData(): Flow<List<SavedDataFormula>> {
        val flowData= flow {
            val storedLocatios=location.toList()
            emit(storedLocatios)
        }
        return flowData
    }

    override suspend fun insertFavData(savedDataFormula: SavedDataFormula) {
        location.add(savedDataFormula)
    }

    override suspend fun deleteFromFav(savedDataFormula: SavedDataFormula) {
        location.remove(savedDataFormula)
    }
    override fun getAlertData(): Flow<List<AlertData>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertIntoAlertTable(alertData: AlertData) {
        TODO("Not yet implemented")
    }

    override suspend fun deletefromAlertTable(alertData: AlertData) {
        TODO("Not yet implemented")
    }
}