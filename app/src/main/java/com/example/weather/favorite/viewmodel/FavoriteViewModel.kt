package com.example.weather.favorite.viewmodel

import androidx.lifecycle.*
import com.example.weather.model.MyResponse
import com.example.weather.model.RepositoryInterface
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel (private val repo: RepositoryInterface): ViewModel() {
    private var _favData: MutableLiveData<List<SavedDataFormula>> = MutableLiveData<List<SavedDataFormula>>()
    val favData: LiveData<List<SavedDataFormula>> = _favData
    init {
        getLocalRepo()
    }

    private fun getLocalRepo() {
        viewModelScope.launch (Dispatchers.IO){
            _favData.postValue(repo.showFavData())
        }
    }
    fun insertIntoFav(savedDataFormula: SavedDataFormula){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertFavData(savedDataFormula)
        }
    }
    fun deleteFromFav(savedDataFormula: SavedDataFormula){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteFromFav(savedDataFormula)
        }
    }


}
