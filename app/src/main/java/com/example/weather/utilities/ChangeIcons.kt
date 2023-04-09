package com.example.weather.utilities

import com.example.weather.R

class ChangeIcons {
    companion object{
        fun newIcon(icon:String):Int=when(icon){
            "01d" -> R.drawable.sunny_preview_rev_1
            "02d" -> R.drawable.sun_cloud_preview_rev_1
            "03d" -> R.drawable.cloudy_preview_rev_1
            "04d" -> R.drawable.most_cloudy_preview_rev_1
            "09d" -> R.drawable.couldy_snow_preview_rev_1
            "10d" -> R.drawable.sun_rain_preview_rev_1
            "11d" -> R.drawable.lighting_preview_rev_1
            "13d" -> R.drawable.snow_preview_rev_1
            "50d" -> R.drawable.windy_preview_rev_1
            "01n" -> R.drawable.moon_preview_rev_1
            "02n" -> R.drawable.coud_moon_preview_rev_1
            "03n" -> R.drawable.cloudy_preview_rev_1
            "04n" -> R.drawable.most_cloudy_preview_rev_1
            "09n" -> R.drawable.couldy_snow_preview_rev_1
            "10n" ->
            else -> {}
        }
    }
}