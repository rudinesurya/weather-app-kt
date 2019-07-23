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

    fun fetchData() {
        disposable.add(
            myRepository.getAllWeatherEntry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _weatherList.postValue(it)
                }, {
                    Timber.e(it)
                })
        )
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
                        localTime = it.location.localtime,
                        temperature = it.current.tempC,
                        condition = it.current.condition.text
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