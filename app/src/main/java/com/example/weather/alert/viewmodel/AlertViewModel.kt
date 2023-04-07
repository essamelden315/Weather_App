package com.example.weather.alert.viewmodel


import androidx.lifecycle.*
import com.example.weather.model.AlertData
import com.example.weather.model.RepositoryInterface
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel (private val repo: RepositoryInterface): ViewModel() {
    private var _alertData: MutableLiveData<List<AlertData>> = MutableLiveData<List<AlertData>>()
    val alertData: LiveData<List<AlertData>> = _alertData
    init {
        getAlertData()
    }

    fun getAlertData() {
        viewModelScope.launch (Dispatchers.IO){
            repo.getAlertData()?.collect{
                _alertData.postValue(it)
            }
        }
    }
    fun insertIntoAlert(alertData: AlertData){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertIntoAlertTable(alertData)
            getAlertData()
        }

    }
    fun deleteFromAlert(alertData: AlertData){
        viewModelScope.launch (Dispatchers.IO){
            repo.deletefromAlertTable(alertData)
            getAlertData()
        }
    }


}
