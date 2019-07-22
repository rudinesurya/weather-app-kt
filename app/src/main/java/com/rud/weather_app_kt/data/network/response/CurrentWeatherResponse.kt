package com.rud.weather_app_kt.data.network.response

data class CurrentWeatherResponse(
    val current: Current,
    val location: Location
)