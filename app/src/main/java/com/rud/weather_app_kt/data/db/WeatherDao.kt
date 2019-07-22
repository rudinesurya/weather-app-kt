package com.rud.weather_app_kt.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rud.weather_app_kt.data.model.Weather
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_table")
    fun getAllWeatherEntry(): Flowable<List<Weather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weather: Weather): Completable

    @Query("DELETE FROM weather_table WHERE id = :id")
    fun remove(id: Long): Completable
}