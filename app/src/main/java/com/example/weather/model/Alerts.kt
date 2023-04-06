package com.example.weather.model

data class Alerts(
    var senderName: String? = null,
    var event: String? = null,
    var start: Int?,
    var end: Int? ,
    var description: String? = null,
    var tags: ArrayList<String> = arrayListOf()

){
    constructor():this(null,null,null,null,null)
}