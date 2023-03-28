package com.example.weather.database

import androidx.room.*
import com.example.weather.model.MyResponse
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from HomeData")
    fun getHomeData():Flow<MyResponse>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHomeData(myResponse: MyResponse)


    @Delete
    suspend fun deleteHomeData(myResponse: MyResponse)
  //  @TypeConverters(Converter::class)
    @Query("select * from dataFormula")
    fun getAllDataFromFavTable():Flow<List<SavedDataFormula>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDataIntoFavTable(savedDataFormula: SavedDataFormula)

    @Delete
    suspend fun deleteDataFromFavTable(savedDataFormula: SavedDataFormula)
}