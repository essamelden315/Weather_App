package com.example.weather.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.MyResponse
import com.example.weather.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface): ViewModel() {
    private var _products: MutableLiveData<MyResponse> = MutableLiveData<MyResponse>()
    val products: LiveData<MyResponse> = _products

    init {
        getLocalRepo()
    }

    fun getLocalRepo() {
        viewModelScope.launch(Dispatchers.IO) {
            _products.postValue(repo.getRetrofitWeatherData())
        }
    }
}
