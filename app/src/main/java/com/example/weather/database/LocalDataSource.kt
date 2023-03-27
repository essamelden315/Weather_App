package com.example.weather.database

import com.example.weather.model.SavedDataFormula

interface LocalDataSource {
    suspend fun showFavData():List<SavedDataFormula>?
    suspend fun insertFavData(savedDataFormula: SavedDataFormula)
    suspend fun deleteFromFav(savedDataFormula: SavedDataFormula)
}