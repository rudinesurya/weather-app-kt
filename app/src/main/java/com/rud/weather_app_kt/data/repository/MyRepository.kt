package com.rud.weather_app_kt.data.repository

import com.rud.weather_app_kt.data.db.WeatherDao
import com.rud.weather_app_kt.data.model.Weather

class MyRepository(private val weatherDao: WeatherDao) {
    fun getAllWeatherEntry() = weatherDao.getAllWeatherEntry()

    suspend fun addWeatherEntry(weather: Weather) {
        weatherDao.upsert(weather)
    }

    suspend fun removeWeatherEntry(id: Long) {
        weatherDao.remove(id)
    }
}