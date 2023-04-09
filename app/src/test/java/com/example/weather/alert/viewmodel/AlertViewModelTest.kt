package com.example.weather.alert.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.MainDispatchersRule
import com.example.weather.favorite.viewmodel.FakeFavRepo
import com.example.weather.favorite.viewmodel.FavoriteViewModel
import com.example.weather.getOrAwaitValue
import com.example.weather.model.AlertData
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()
    lateinit var viewModel : AlertViewModel
    lateinit var repo : FakeFavRepo
    lateinit var dummyData1: AlertData
    lateinit var dummyData2: AlertData

    fun initializeVariables(){
        dummyData1 = AlertData("","","","",10L,10L,10L,10L,10L)
        dummyData2 = AlertData("","","","",10L,10L,10L,10L,10L)
    }
    @Before
    fun setUp(){
        initializeVariables()
        repo = FakeFavRepo()
        viewModel = AlertViewModel(repo)
    }
    @Test
    fun getAlert_retrunAllDataFromAlertTable()= mainDispatchersRule.runBlockingTest{
        //given
        viewModel.insertIntoAlert(dummyData1)
        //then
        var result=viewModel.alertData.getOrAwaitValue {  }
        //result
        assertThat(result,`is`(listOf(dummyData1)))
    }
    @Test
    fun insertDataIntoAlertTable(){
        //given
        viewModel.insertIntoAlert(dummyData1)
        viewModel.insertIntoAlert(dummyData2)
        //when
        viewModel.getAlertData()
        var result=viewModel.alertData.getOrAwaitValue {  }
        //then
        assertThat(result, CoreMatchers.not(emptyList()))
    }
    @Test
    fun deleteDataFromFavTable(){
        //given
        viewModel.insertIntoAlert(dummyData1)
        viewModel.deleteFromAlert(dummyData1)
        //when
        viewModel.getAlertData()
        var result=viewModel.alertData.getOrAwaitValue {  }
        //then
        assertThat(result,`is`(emptyList()))
    }
}