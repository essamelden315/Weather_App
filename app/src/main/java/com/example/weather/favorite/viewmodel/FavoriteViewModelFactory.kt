package com.example.weather.favorite.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.home.viewmodel.HomeViewModel
import com.example.weather.model.RepositoryInterface

class FavoriteViewModelFactory (private val irepo: RepositoryInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {

            FavoriteViewModel(irepo) as T

        } else {

            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}