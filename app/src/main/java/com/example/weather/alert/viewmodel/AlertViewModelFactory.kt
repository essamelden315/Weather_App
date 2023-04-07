package com.example.weather.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.RepositoryInterface

class AlertViewModelFactory (private val irepo: RepositoryInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {

            AlertViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}