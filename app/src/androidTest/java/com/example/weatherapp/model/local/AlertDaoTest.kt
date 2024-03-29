package com.example.weatherapp.model.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.example.weatherapp.TestDispatcherRule
import com.example.weatherapp.model.data.local.AlertDao
import com.example.weatherapp.model.data.local.WeatherDatabase
import com.example.weatherapp.model.pojo.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDaoTest {

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var database: WeatherDatabase
    private lateinit var dao: AlertDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.alertDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAlertItem_shouldReturn_theItem_inFlow() = runTest  {
        val alert = WeatherAlert(
            1,
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong()
        )
        dao.insertAlert(alert)

        dao.getAllAerts().test {
            val list = awaitItem()
            assert(list.contains(alert))
            cancel()
        }
    }

    @Test
    fun deletedWeatherItem_shouldNot_be_present_inFlow() = runTest {
        val alert1 = WeatherAlert(
            1,
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong()
        )
        val alert2 = WeatherAlert(
            2,
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong()
        )

        dao.insertAlert(alert1)
        dao.insertAlert(alert2)
        dao.deleteAlert(alert2)

        dao.getAllAerts().test  {
            val list = awaitItem()
            assert(list.size == 1)
            assert(list.contains(alert1))
            cancel()
        }
    }

    @Test
    fun updateItem_shouldReturn_theItem_inFlow() = runTest {
        val alert1 = WeatherAlert(
            1,
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong()
        )
        val alert2 = WeatherAlert(
            2,
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong(),
            0.0.toLong()
        )

        dao.insertAlert(alert1)
        dao.insertAlert(alert2)

        dao.getAllAerts().test {
            val list = awaitItem()
            assert(list.size == 2)
            assert(list.contains(alert1))
            assert(list.contains(alert2))
            cancel()
        }
    }
}