package com.rud.weather_app_kt.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class Weather(
    val locationName: String,
    val temperature: Float,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)