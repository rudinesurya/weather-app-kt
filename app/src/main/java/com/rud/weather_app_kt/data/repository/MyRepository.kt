package com.rud.weather_app_kt.data.repository

import com.rud.weather_app_kt.data.db.WeatherDao
import com.rud.weather_app_kt.data.model.Weather
import com.rud.weather_app_kt.data.network.ApixuDataSource

class MyRepository(private val weatherDao: WeatherDao, private val apixuDataSource: ApixuDataSource) {
    fun getAllWeatherEntry() = weatherDao.getAllWeatherEntry()

    fun addWeatherEntry(weather: Weather) = weatherDao.upsert(weather)

    fun removeWeatherEntry(id: Long) = weatherDao.remove(id)

    fun getCurrentWeather(location: String) = apixuDataSource.getCurrentWeather(location)
}