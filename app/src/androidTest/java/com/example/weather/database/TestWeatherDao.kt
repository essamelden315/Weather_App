package com.example.weather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weather.getOrAwaitValue
import com.example.weather.model.SavedDataFormula
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TestWeatherDao {
    @get:Rule
    val instance: InstantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: AppDataBase

    @Before
    fun createDataBase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).build()
    }
    @After
    fun clean() = database.close()

    @Test
    fun insertFavAndGetData() = runBlocking {
        //Given
        val data = SavedDataFormula(31.0, 31.0, "alexandria")
        val data2= SavedDataFormula(33.0, 33.0, "cairo")
        val list = listOf(data,data2)
        database.weatherDao().insertDataIntoFavTable(data)
        database.weatherDao().insertDataIntoFavTable(data2)
        // when get tha data
         val result = database.weatherDao().getAllDataFromFavTable().getOrAwaitValue {  }
        // then load the data contains the expected value
        assertThat(result.get(0).myLatitude,`is`(data.myLatitude))
        assertThat(result.get(0).myLongitude, `is`(data.myLongitude))
        assertThat(result.get(0).locationName, `is`(data.locationName))
        Assert.assertEquals("",list,result)
    }

    @Test
    fun deleteFavFromDataBase()= runBlocking{
        //Given   insert task
        val data = SavedDataFormula(31.0, 31.0, "alexandria")
        database.weatherDao().insertDataIntoFavTable(data)
        database.weatherDao().deleteDataFromFavTable(data)
        // when get tha data
        val result = database.weatherDao().getAllDataFromFavTable().getOrAwaitValue {  }
        // then load the data contains the expected value
        assertThat(result,`is`(emptyList()))

    }
}