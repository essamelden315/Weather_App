package com.example.weather.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class TimeConverter {
    companion object{
        fun convertHomeDate(time: Long?): String {
            val simpleDate = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = simpleDate.format(time?.times(1000))
            val date1: Date = simpleDate.parse(currentDate)
            val split = date1.toString().split(" ")
            val myDate = "${split[0]},${split[2]} ${split[1]} "
            return myDate
        }
         fun convertToDayHours(date: Long): String {
            val date = Date(date * 1000L)
            val sdf = SimpleDateFormat("hh:mm a")
            sdf.timeZone = TimeZone.getDefault()
            val formatedData = sdf.format(date)
            return formatedData.toString()
        }
         fun convertToWeekDays(date: Long): String {
            val simpleDate = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = simpleDate.format(date.times(1000))
            val date: Date = simpleDate.parse(currentDate)
            val outFormat = SimpleDateFormat("EEEE")
            val goal = outFormat.format(date)
            return goal.toString()
        }
    }

}