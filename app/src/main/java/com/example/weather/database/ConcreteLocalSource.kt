package com.example.weather.database

import android.content.Context
import com.example.weather.model.SavedDataFormula

class ConcreteLocalSource private constructor(var context: Context):LocalDataSource {
    companion object{
        private var myInstance:ConcreteLocalSource ?= null
        fun getInstance(ctx:Context):ConcreteLocalSource?{
            if(myInstance==null)
                myInstance = ConcreteLocalSource(ctx)
            return myInstance
        }
    }
    val myWeatherDoa:WeatherDao? by lazy {
        AppDataBase.getInstance(context)?.weatherDao()
    }
    override suspend fun showFavData(): List<SavedDataFormula>? {
        return myWeatherDoa?.getAllDataFromFavTable()
    }

    override suspend fun insertFavData(savedDataFormula: SavedDataFormula) {
        myWeatherDoa?.insertDataIntoFavTable(savedDataFormula)
    }

    override suspend fun deleteFromFav(savedDataFormula: SavedDataFormula) {
        myWeatherDoa?.deleteDataFromFavTable(savedDataFormula)
    }
}