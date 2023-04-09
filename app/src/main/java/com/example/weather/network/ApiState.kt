package com.example.weather.network
import com.example.weather.model.MyResponse

sealed class ApiState {
    class Success(var myResponse: MyResponse?):ApiState()
    class Failure (var msg :Throwable):ApiState()
    object Loading:ApiState()
}