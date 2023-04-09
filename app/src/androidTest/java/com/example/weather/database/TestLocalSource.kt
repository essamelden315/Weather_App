package com.example.weather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weather.getOrAwaitValue
import com.example.weather.model.AlertData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
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