package com.rud.weather_app_kt.ui.weather_list

import androidx.lifecycle.ViewModel
import com.rud.weather_app_kt.data.model.Weather
import com.rud.weather_app_kt.data.repository.MyRepository
import com.rud.weather_app_kt.ui.utils.lazyDeferred

class WeatherListViewModel(private val myRepository: MyRepository) : ViewModel() {
    val weatherList by lazyDeferred {
        myRepository.getAllWeatherEntry()
    }

    suspend fun insertWeatherEntry(weather: Weather) {
        myRepository.addWeatherEntry(weather)
    }

    suspend fun removeWeatherEntry(weather: Weather) {
        myRepository.removeWeatherEntry(weather.id)
    }
}