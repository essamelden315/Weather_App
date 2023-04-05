package com.example.weather.alert.viewmodel

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.model.MyResponse
import com.example.weather.model.Repository
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*class AlertViewModel: ViewModel() {
    private var _alertData: MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
    val alertData: LiveData<MyResponse> = _alertData
    val repo =  Repository.getInstance(
        WeatherClient.getInstance() as RemoteSource,
        ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource
    ) as Repository
    fun getWeatherData(lati:Double, longi:Double,language:String,unit:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _alertData.postValue(repo.getRetrofitWeatherData(lati,longi,"minutely",language,unit))
        }
}*/
