package com.example.weather.model

data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
){
    constructor():this(0.0,0.0,0.0,0.0,0.0,0.0)
}