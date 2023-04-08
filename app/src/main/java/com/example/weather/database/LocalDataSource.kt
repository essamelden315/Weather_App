package com.example.weather.database

import com.example.weather.model.AlertData
import com.example.weather.model.MyResponse
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getHomeData():Flow<MyResponse>
    suspend fun insertHomeData(myResponse: MyResponse)
    suspend fun deleteHomeData()
    fun showFavData():Flow<List<SavedDataFormula>>
    suspend fun insertFavData(savedDataFormula: SavedDataFormula)
    suspend fun deleteFromFav(savedDataFormula: SavedDataFormula)
    fun getAlertData():Flow<List<AlertData>>
    suspend fun insertIntoAlertTable(alertData: AlertData)
    suspend fun deletefromAlertTable(alertData: AlertData)

}