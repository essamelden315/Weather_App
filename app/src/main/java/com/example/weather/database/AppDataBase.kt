package com.example.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.model.MyResponse
import com.example.weather.model.SavedDataFormula

@Database(arrayOf(SavedDataFormula::class), version = 1)
abstract class AppDataBase :RoomDatabase(){
    abstract fun weatherDao():WeatherDao

    companion object{
        @Volatile
        private var myInstance:AppDataBase? = null
        fun getInstance(context: Context):AppDataBase?{
            return myInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDataBase::class.java, "weatherDB"
                ).build()
                myInstance = instance
                instance
            }
        }
    }
}