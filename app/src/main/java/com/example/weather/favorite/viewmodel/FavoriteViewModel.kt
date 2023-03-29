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

     fun getLocalRepo() {
        viewModelScope.launch (Dispatchers.IO){
                repo.showFavData()?.collect{
                    _favData.postValue(it)
                }
        }
    }
    fun insertIntoFav(savedDataFormula: SavedDataFormula){
        viewModelScope.launch (Dispatchers.IO){
                repo.insertFavData(savedDataFormula)
                getLocalRepo()
        }

    }
    fun deleteFromFav(savedDataFormula: SavedDataFormula){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteFromFav(savedDataFormula)
            getLocalRepo()
        }
    }


}
