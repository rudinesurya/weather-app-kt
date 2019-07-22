package com.rud.weather_app_kt.ui.weather_list

import androidx.lifecycle.ViewModel
import com.rud.weather_app_kt.data.model.Weather
import com.rud.weather_app_kt.data.repository.MyRepository

class WeatherListViewModel(private val myRepository: MyRepository) : ViewModel() {
    fun getAllWeatherEntries() = myRepository.getAllWeatherEntry()

    fun insertWeatherEntry(weather: Weather) = myRepository.addWeatherEntry(weather)

    fun removeWeatherEntry(weather: Weather) = myRepository.removeWeatherEntry(weather.id)
}