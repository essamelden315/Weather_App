package com.example.weather.favorite.view

import com.example.weather.model.SavedDataFormula

interface ListnerInterface {
    fun insertData(savedDataFormula: SavedDataFormula)
    fun deleteDatafromDB(savedDataFormula: SavedDataFormula)
}