package com.example.weather.database

import androidx.room.*
import com.example.weather.model.AlertData
import com.example.weather.model.MyResponse
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from HomeData")
    fun getHomeData():Flow<MyResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeData(myResponse: MyResponse)

    @Query("DELETE from HomeData")
    suspend fun deleteHomeData()

    @Query("select * from dataFormula")
    fun getAllDataFromFavTable():Flow<List<SavedDataFormula>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDataIntoFavTable(savedDataFormula: SavedDataFormula)

    @Delete
    suspend fun deleteDataFromFavTable(savedDataFormula: SavedDataFormula)

    @Query("select * from AlertData")
    fun getAllDataFromAlert():Flow<List<AlertData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDataIntoAlertTable(alertData: AlertData)

    @Delete
    fun deleteDataFromAlertTable(alertData: AlertData)
}