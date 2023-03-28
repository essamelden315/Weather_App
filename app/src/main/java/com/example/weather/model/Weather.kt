package com.example.weather.model

data class Weather (
    val id: Long,
    val main: String?,
    val description: String?,
    val icon: String?
){
    constructor():this(0L,null,null,null)
}