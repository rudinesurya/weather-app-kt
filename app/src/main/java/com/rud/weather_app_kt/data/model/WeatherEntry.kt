package com.rud.weather_app_kt.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntry(
    @PrimaryKey(autoGenerate = false)
    val city: String,
    val timezone: String,
    val temperature: Double,
    val conditionCode: Int
)