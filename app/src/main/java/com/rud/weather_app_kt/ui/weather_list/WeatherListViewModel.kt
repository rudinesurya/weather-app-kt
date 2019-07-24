package com.rud.weather_app_kt.ui.weather_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rud.weather_app_kt.data.model.WeatherEntry
import com.rud.weather_app_kt.data.repository.MyRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class WeatherListViewModel(private val myRepository: MyRepository) : ViewModel() {

    private val _weatherList = MutableLiveData<List<WeatherEntry>>()
    val weatherList: LiveData<List<WeatherEntry>>
        get() = _weatherList

    private val disposable = CompositeDisposable()
    var updated = false

    init {
        fetchData()
    }

    fun fetchData(onComplete: () -> Unit = {}) {
        Timber.d("fetching weather entries")

        disposable.add(
            myRepository.getAllWeatherEntry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    // update all entry with latest info from remote server
                    if (!updated && it.isNotEmpty()) {
                        updated = true

                        Timber.d("Updating all weather entries")
                        updateWeatherEntries(it)
                    }

                    _weatherList.postValue(it)
                    onComplete()
                }, {
                    Timber.e(it)
                })
        )
    }

    private fun updateWeatherEntries(weatherEntries: List<WeatherEntry>) {
        weatherEntries.forEach {
            addWeatherEntry(it.city)
        }
    }

    private fun createWeatherEntry(
        locationName: String,
        onComplete: (WeatherEntry) -> Unit = {},
        onError: () -> Unit = {}
    ) {
        val upperCase = locationName.trim().toUpperCase()

        // fetch weather data
        disposable.add(
            myRepository.getCurrentWeather(locationName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    // create the weather entry
                    val weather = WeatherEntry(
                        city = upperCase,
                        timezone = it.location.tzId,
                        temperature = it.current.tempC,
                        conditionCode = it.current.condition.code
                    )

                    onComplete(weather)
                }, {
                    Timber.e(it)
                    onError()
                })
        )
    }

    fun addWeatherEntry(locationName: String, onComplete: () -> Unit = {}, onError: () -> Unit = {}) {
        createWeatherEntry(locationName, onComplete = {
            addWeatherEntryToDb(it, onComplete = {
                onComplete()
            }, onError = {
                onError()
            })
        }, onError = {
            onError()
        })
    }

    fun addWeatherEntryToDb(weather: WeatherEntry, onComplete: () -> Unit = {}, onError: () -> Unit = {}) {
        disposable.add(
            myRepository.addWeatherEntry(weather)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onComplete()
                }, {
                    Timber.e(it)
                    onError()
                })
        )
    }

    fun removeWeatherEntry(weatherEntry: WeatherEntry, onComplete: () -> Unit = {}, onError: () -> Unit = {}) {
        disposable.add(
            myRepository.removeWeatherEntry(weatherEntry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onComplete()
                }, {
                    Timber.e(it)
                    onError()
                })
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}