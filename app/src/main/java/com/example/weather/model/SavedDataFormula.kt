package com.example.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataFormula")
data class SavedDataFormula(
        var myLatitude:Double,
        var myLongitude:Double,
        @PrimaryKey
        var locationName:String
)