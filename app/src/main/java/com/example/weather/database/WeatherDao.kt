package com.example.weather.database

import androidx.room.*
import com.example.weather.model.SavedDataFormula

@Dao
interface WeatherDao {
    @Query("select * from dataFormula")
    suspend fun getAllDataFromFavTable():List<SavedDataFormula>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDataIntoFavTable(savedDataFormula: SavedDataFormula)

    @Delete
    suspend fun deleteDataFromFavTable(savedDataFormula: SavedDataFormula)
}