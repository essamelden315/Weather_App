package com.example.weather.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.MainDispatchersRule
import com.example.weather.database.LocalDataSource
import com.example.weather.network.RemoteSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()
    lateinit var localData:LocalDataSource
    lateinit var remoteData:RemoteSource
    lateinit var repo:Repository
    lateinit var myResponse: MyResponse

    fun initializeVariables(){
        myResponse = MyResponse()
    }
    @Before
    fun setUp(){
        initializeVariables()
        localData = FakeLocal()
        remoteData = FakeRemote()
        repo = Repository.getInstance(remoteData ,localData)!!
    }

    @Test
    fun getDataFromRetrofit()= runBlocking{
        //given
        val expected = myResponse
        //when
         repo.getRetrofitList(31.0,31.0,"","","").collect{
            //then
            assertEquals(myResponse,it)
        }
    }

    @Test
    fun insertFavData_UsingRepo()= runBlocking{
        //given
        val data= SavedDataFormula(31.0,31.0,"alexandria")
        repo.insertFavData(data)
        //when
         repo.showFavData().collect{
            //then
            assertThat(it,`is`(not(emptyList())))
        }
    }
    @Test
    fun deleteFromFav_UsingRepo()= runBlocking{
        //given
        val data= SavedDataFormula(31.0,31.0,"alexandria")
        repo.insertFavData(data)
        //when
        repo.deleteFromFav(data)
         repo.showFavData().collect{
            //then
            assertThat(it,`is`(emptyList()))
        }
    }

}