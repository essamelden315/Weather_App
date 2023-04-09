package com.example.weather.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.MainDispatchersRule
import com.example.weather.getOrAwaitValue
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    lateinit var viewModel :FavoriteViewModel
    lateinit var repo : FakeFavRepo
    lateinit var dummyData1:SavedDataFormula
    lateinit var dummyData2:SavedDataFormula

    fun initializeVariables(){
        dummyData1 = SavedDataFormula(31.0,31.0,"cairo")
        dummyData2 = SavedDataFormula(62.0,64.0,"paris")
    }

    @Before
    fun setUp(){
        initializeVariables()
        repo = FakeFavRepo()
        viewModel = FavoriteViewModel(repo)
    }


    @Test
    fun getFavData_retrunAllDataFromFAVTable()= runBlocking{
        //given
        viewModel.insertIntoFav(dummyData1)
        viewModel.insertIntoFav(dummyData2)
        //then
        var result=viewModel.favData.getOrAwaitValue {  }
        //result
        assertThat(result,`is`(listOf(dummyData1,dummyData2)))
    }

    @Test
    fun insertDataIntoFavTable_TableFilledWithData(){
        //given
        viewModel.insertIntoFav(dummyData1)
        viewModel.insertIntoFav(dummyData2)
        //when
        viewModel.getLocalRepo()
        var result=viewModel.favData.getOrAwaitValue {  }
        //then
        assertThat(result,not(emptyList()))
    }

    @Test
    fun deleteDataFromFavTable(){
        //given
        viewModel.insertIntoFav(dummyData1)
        viewModel.deleteFromFav(dummyData1)
        //when
        viewModel.getLocalRepo()
        var result=viewModel.favData.getOrAwaitValue {  }
        //then
        assertThat(result,`is`(emptyList()))
    }
}