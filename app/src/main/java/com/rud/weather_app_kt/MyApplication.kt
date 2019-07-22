package com.rud.weather_app_kt

import android.app.Application
import com.rud.weather_app_kt.data.db.WeatherDatabase
import com.rud.weather_app_kt.data.repository.MyRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class MyApplication : Application(), KodeinAware {
    init {
        Timber.plant(Timber.DebugTree())
        Timber.d("Timber planted")
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().weatherDao() }
        bind() from singleton { MyRepository(instance()) }
        bind() from provider { ViewModelFactory(instance(), instance()) }
    }
}