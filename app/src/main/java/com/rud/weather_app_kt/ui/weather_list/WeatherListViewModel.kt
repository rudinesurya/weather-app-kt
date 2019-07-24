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

    fun fetchData(onComplete: () -> Unit = {}) {
        disposable.add(
            myRepository.getAllWeatherEntry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    // update all entry with latest info from remote server
                    if (!updated) {
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

    private fun updateWeatherEntries(weatherEntries: List<WeatherEntry>?) {
        weatherEntries?.forEach {
            addWeatherEntry(it.city)
        }
    }

    fun addWeatherEntry(locationName: String, onComplete: () -> Unit = {}, onError: () -> Unit = {}) {
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

                    Timber.d(weather.toString())

                    disposable.add(
                        myRepository.addWeatherEntry(weather)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Timber.d("add success")
                                onComplete()
                            }, {
                                Timber.e(it)
                                onError()
                            })
                    )
                }, {
                    Timber.e(it)
                    onError()
                })
        )
    }

    fun removeWeatherEntry(weatherEntry: WeatherEntry, onComplete: () -> Unit = {}) {
        disposable.add(
            myRepository.removeWeatherEntry(weatherEntry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("remove success")
                    onComplete()
                }, {
                    Timber.e(it)
                })
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}