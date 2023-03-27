package com.example.weather.favorite.view

import com.example.weather.model.SavedDataFormula

interface ListnerInterface {
    fun deleteDatafromDB(savedDataFormula: SavedDataFormula)
}