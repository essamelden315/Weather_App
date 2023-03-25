package com.example.weather.home.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.weather.model.MyResponse
import com.example.weather.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModelFactory(private val irepo: RepositoryInterface,private val context: Context) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {

            HomeViewModel(irepo,context) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}
