package com.rud.weather_app_kt.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rud.weather_app_kt.data.model.WeatherEntry
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@Database(
    entities = [WeatherEntry::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java, "weather_app.db"
        ).addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                Timber.d("Seeding db..")

                val seedData = listOf(
                    WeatherEntry(city = "DUBLIN", timezone = "", temperature = 0.0, conditionCode = 0),
                    WeatherEntry(city = "LONDON", timezone = "", temperature = 0.0, conditionCode = 0),
                    WeatherEntry(city = "BEIJING", timezone = "", temperature = 0.0, conditionCode = 0),
                    WeatherEntry(city = "SYDNEY", timezone = "", temperature = 0.0, conditionCode = 0)
                )

                instance?.weatherDao()?.upsertAll(seedData)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe {
                        Timber.d("Db seeded..")
                    }
            }
        }).build()
    }
}