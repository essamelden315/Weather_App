package com.example.weather.database

import com.example.weather.model.MyResponse
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getHomeData():Flow<MyResponse>?
    suspend fun insertHomeData(myResponse: MyResponse)
    suspend fun deleteHomeData(myResponse: MyResponse)
    fun showFavData():Flow<List<SavedDataFormula>>?
    suspend fun insertFavData(savedDataFormula: SavedDataFormula)
    suspend fun deleteFromFav(savedDataFormula: SavedDataFormula)

}