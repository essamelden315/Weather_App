package com.example.weather.home.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.home.view.GetMyLocation
import com.example.weather.model.MyResponse
import com.example.weather.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface,private val context: Context): ViewModel() {
    private var _homeData: MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
    val homeData: LiveData<MyResponse> = _homeData


    fun getWeatherData(lati:Double, longi:Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _homeData.postValue(repo.getRetrofitWeatherData(lati,longi,"minutely","en","metric"))
        }

    }
     fun getLatitude_Longitude(){
        var getMyLocation:GetMyLocation = GetMyLocation(context)
        getMyLocation.getLastLocation()
        getMyLocation.location.observe(context as LifecycleOwner){
            getWeatherData(it.get(0),it.get(1))
        }
    }
    fun getHomeDataFromDataBase(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getHomeData()?.collect{
                _homeData.postValue(it)
            }
        }


    }
     fun insertHomeDataIntoDataBase(myResponse: MyResponse){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertHomeData(myResponse)
        }

    }
     fun deleteHomeDaraFromDataBase(myResponse: MyResponse){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteHomeData(myResponse)
        }
    }
}
