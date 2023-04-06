package com.example.weather.model

import androidx.room.Embedded
import androidx.room.Entity


@Entity(tableName = "HomeData", primaryKeys =["lat","lon"])
data class MyResponse (
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    @Embedded
    val current: Current? = null,
    val hourly: List<Current>,
    val daily: List<Daily>,
    var alerts: List<Alerts> = arrayListOf()
){
    constructor():this(0.0 , 0.0 , "" ,0L ,null, listOf() , listOf())
}


