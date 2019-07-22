package com.rud.weather_app_kt.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rud.weather_app_kt.R
import com.rud.weather_app_kt.ui.weather_list.WeatherListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().add(R.id.screenContainer, WeatherListFragment()).commit()
    }
}
