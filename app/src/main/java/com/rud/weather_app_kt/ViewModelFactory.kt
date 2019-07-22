package com.rud.weather_app_kt

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rud.weather_app_kt.data.repository.MyRepository
import com.rud.weather_app_kt.ui.weather_list.WeatherListViewModel

class ViewModelFactory(
    private val app: Application,
    private val myRepository: MyRepository
) : ViewModelProvider.NewInstanceFactory() {
    @SuppressWarnings("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {

            WeatherListViewModel::class.java -> WeatherListViewModel(myRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}