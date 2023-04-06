package com.example.weather.database

import androidx.room.TypeConverter
import com.example.weather.model.Alerts
import com.example.weather.model.Current
import com.example.weather.model.Daily
import com.example.weather.model.Weather
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun listOfCurrentToJson(value: List<Current>) = Gson().toJson(value)
    @TypeConverter
    fun jsonToListOfCurrent(value: String ) =Gson().fromJson(value,Array<Current>::class.java).toList()
    @TypeConverter
    fun listOfDailyToJson(value: List<Daily>) = Gson().toJson(value)
    @TypeConverter
    fun jsonToListOfDaily(value: String ) =Gson().fromJson(value,Array<Daily>::class.java).toList()
    @TypeConverter
    fun listOfWeatherToJson(value: List<Weather> ) =Gson().toJson(value)
    @TypeConverter
    fun JsonToListOfWeather(value: String ) =Gson().fromJson(value,Array<Weather>::class.java).toList()
    @TypeConverter
    fun listOfAlertsToJson(value: List<Alerts> ) =Gson().toJson(value)
    @TypeConverter
    fun JsonToListOfAlerts(value: String ) =Gson().fromJson(value,Array<Alerts>::class.java).toList()

}