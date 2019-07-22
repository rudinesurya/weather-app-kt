package com.rud.weather_app_kt.data.network.response

data class Condition(
    val code: Int,
    val icon: String,
    val text: String
)