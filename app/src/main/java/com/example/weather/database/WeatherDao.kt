package com.example.weather.database

import androidx.room.*
import com.example.weather.model.MyResponse
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from HomeData")
    fun getHomeData():Flow<MyResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeData(myResponse: MyResponse)

    //  @TypeConverters(Converter::class)
    @Delete
    suspend fun deleteHomeData(myResponse: MyResponse)

    @Query("select * from dataFormula")
    fun getAllDataFromFavTable():Flow<List<SavedDataFormula>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDataIntoFavTable(savedDataFormula: SavedDataFormula)

    @Delete
    suspend fun deleteDataFromFavTable(savedDataFormula: SavedDataFormula)
}