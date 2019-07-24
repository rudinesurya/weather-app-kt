package com.rud.weather_app_kt.data.repository

import com.rud.weather_app_kt.data.db.WeatherDao
import com.rud.weather_app_kt.data.model.WeatherEntry
import com.rud.weather_app_kt.data.network.ApixuDataSource

class MyRepository(private val weatherDao: WeatherDao, private val apixuDataSource: ApixuDataSource) {
    fun getAllWeatherEntry() = weatherDao.getAllWeatherEntry()

    fun addWeatherEntry(weatherEntry: WeatherEntry) = weatherDao.upsert(weatherEntry)

    fun addWeatherEntries(weatherEntries: List<WeatherEntry>) = weatherDao.upsertAll(weatherEntries)

    fun removeWeatherEntry(weatherEntry: WeatherEntry) = weatherDao.remove(weatherEntry.city)

    fun getCurrentWeather(location: String) = apixuDataSource.getCurrentWeather(location)
}