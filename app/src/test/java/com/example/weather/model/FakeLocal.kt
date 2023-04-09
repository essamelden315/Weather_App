package com.example.weather.model

import com.example.weather.database.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocal:LocalDataSource {
    var favData :MutableList<SavedDataFormula> = mutableListOf()

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
            val storedLocatios=favData.toList()
            emit(storedLocatios)
        }
        return flowData
    }

    override suspend fun insertFavData(savedDataFormula: SavedDataFormula) {
        favData.add(savedDataFormula)
    }

    override suspend fun deleteFromFav(savedDataFormula: SavedDataFormula) {
        favData.remove(savedDataFormula)
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