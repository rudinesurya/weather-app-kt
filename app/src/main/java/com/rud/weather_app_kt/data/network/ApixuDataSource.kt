package com.rud.weather_app_kt.data.network

class ApixuDataSource(private val apixuService: ApixuService) {
    fun getCurrentWeather(location: String) = apixuService.getCurrentWeather(location)
}