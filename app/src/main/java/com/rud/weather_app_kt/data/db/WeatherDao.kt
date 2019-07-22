package com.rud.weather_app_kt.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rud.weather_app_kt.data.model.Weather

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: Weather): Long

    @Query("SELECT * FROM weather_table")
    fun getAllWeatherEntry(): LiveData<List<Weather>>

    @Query("DELETE FROM weather_table WHERE id = :id")
    suspend fun remove(id: Long)
}