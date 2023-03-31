package com.example.weather.utilities

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import androidx.fragment.app.FragmentActivity
import com.example.weather.R
import java.util.*
class FacilitateWork{
    companion object{
        fun locale(lang:String,resources:Resources){
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        fun getTempUnitAndSign(temp:String,activity:FragmentActivity):Pair<String,String>{
            var units = ""
            var degreeType =""
            when(temp){
                "cel" -> {
                    units = "metric"
                    degreeType = activity.getString(R.string.CelSign)
                }
                "kel" -> {
                    units = "standard"
                    degreeType = activity.getString(R.string.KelSign)
                }
                else -> {
                    units = "imperial"
                    degreeType = activity.getString(R.string.FehSign)
                }
            }
            return Pair(units,degreeType)
        }
    }

}
