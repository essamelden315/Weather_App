package com.example.weather.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.home.view.GetMyLocation
import com.example.weather.model.MyResponse
import com.example.weather.model.RepositoryInterface
import com.example.weather.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface,private val context: Context): ViewModel() {

   private var _homeData: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val homeData: StateFlow<ApiState> = _homeData

     fun getLatitude_Longitude(language:String,unit:String){
        var getMyLocation:GetMyLocation = GetMyLocation(context)
        getMyLocation.getLastLocation()
        getMyLocation.location.observe(context as LifecycleOwner){
            getWeatherData(it.get(0),it.get(1),language,unit)
        }

    }
     fun getWeatherData(lati:Double, longi:Double,language:String,unit:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getRetrofitList(lati,longi,"minutely",language,unit).catch { e->
                _homeData.value = ApiState.Failure(e)
            }?.collect{
                if(it.isSuccessful && it.body()!=null) {
                    _homeData.value = ApiState.Success(it.body()!!)
                }
                else {
                    _homeData.value = ApiState.Failure(java.lang.NullPointerException())
                }
            }

        }

    }
    fun getHomeDataFromDataBase(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getHomeData().catch {e ->
                _homeData.value = ApiState.Failure(e)
            }.collect{
                _homeData.value = ApiState.Success(it)
            }
        }


    }
     fun insertHomeDataIntoDataBase(myResponse: MyResponse){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertHomeData(myResponse)
        }

    }
     fun deleteHomeDaraFromDataBase(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteHomeData()
        }
    }
}
