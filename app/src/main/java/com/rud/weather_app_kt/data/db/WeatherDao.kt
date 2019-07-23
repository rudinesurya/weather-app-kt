package com.rud.weather_app_kt.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rud.weather_app_kt.data.model.WeatherEntry
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_table ORDER BY city ASC")
    fun getAllWeatherEntry(): Flowable<List<WeatherEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: WeatherEntry): Completable

    @Query("DELETE FROM weather_table WHERE city = :city")
    fun remove(city: String): Completable
}