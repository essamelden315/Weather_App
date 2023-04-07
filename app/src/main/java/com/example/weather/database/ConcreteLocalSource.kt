package com.example.weather.database

import android.content.Context
import com.example.weather.model.AlertData
import com.example.weather.model.MyResponse
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.flow.Flow

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

    override fun getHomeData(): Flow<MyResponse>? {
        return myWeatherDoa?.getHomeData()
    }

    override suspend fun insertHomeData(myResponse: MyResponse) {
        myWeatherDoa?.insertHomeData(myResponse)
    }

    override suspend fun deleteHomeData() {
        myWeatherDoa?.deleteHomeData()
    }
    override fun showFavData(): Flow<List<SavedDataFormula>>? {
        return myWeatherDoa?.getAllDataFromFavTable()
    }



    override suspend fun insertFavData(savedDataFormula: SavedDataFormula) {
        myWeatherDoa?.insertDataIntoFavTable(savedDataFormula)
    }

    override suspend fun deleteFromFav(savedDataFormula: SavedDataFormula) {
        myWeatherDoa?.deleteDataFromFavTable(savedDataFormula)
    }

    override fun getAlertData(): Flow<List<AlertData>>? {
        return myWeatherDoa?.getAllDataFromAlert()
    }

    override suspend fun insertIntoAlertTable(alertData: AlertData) {
        myWeatherDoa?.insertDataIntoAlertTable(alertData)
    }

    override suspend fun deletefromAlertTable(alertData: AlertData) {
        myWeatherDoa?.deleteDataFromAlertTable(alertData)
    }


}