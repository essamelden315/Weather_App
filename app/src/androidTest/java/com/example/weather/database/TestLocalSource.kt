package com.example.weather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.runner.AndroidJUnitRunner
import com.example.weather.getOrAwaitValue
import com.example.weather.model.AlertData
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TestLocalSource {
    @get:Rule
    val instance: InstantTaskExecutorRule = InstantTaskExecutorRule()
    lateinit var local:ConcreteLocalSource
    private lateinit var database: AppDataBase

    @Before
    fun setup(){
        local = ConcreteLocalSource.getInstance(ApplicationProvider.getApplicationContext())!!
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).build()
    }
    @Test
    fun insertAlertData() = runBlockingTest {
        //Given
        val data = AlertData(id = 3,"","","","",10L,10L,10L,10L,10L)
        local.insertIntoAlertTable(data)
        // when
        val result =local.getAlertData().getOrAwaitValue {  }
        // then load the data contains the expected value
        Assertions.assertThat(result).contains(data)
        local.deletefromAlertTable(data)
    }
    @Test
    fun deleteAlertData() = runBlockingTest {
        //Given
        val data = AlertData(id = 3,"","","","",10L,10L,10L,10L,10L)
        local.insertIntoAlertTable(data)
        local.deletefromAlertTable(data)
        // when
        val result =local.getAlertData().getOrAwaitValue {  }
        // then load the data contains the expected value
        Assertions.assertThat(result).doesNotContain(data)
    }
}