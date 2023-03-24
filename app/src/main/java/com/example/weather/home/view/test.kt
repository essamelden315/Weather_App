package com.example.weather.home.view

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


fun main() {
    var da:Long =1679583436
    var simpleDate = SimpleDateFormat("dd-MM-yyyy")
    var currentDate = simpleDate.format(da.times(1000))
    var date1: Date = simpleDate.parse(currentDate)
    var outFormat = SimpleDateFormat("EEEE")
    var goal = outFormat.format(date1)
    println(date1.toString().split(" ")[0])
    var date2= Date(da*1000L)
    var sdf= SimpleDateFormat("hh:mm a")
    sdf.timeZone=TimeZone.getDefault()
    var formatedData=sdf.format(date2)
    println(formatedData)
   // Log.i("TAG", currentDate)

}