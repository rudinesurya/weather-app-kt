package com.rud.weather_app_kt.ui.weather_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rud.weather_app_kt.R
import com.rud.weather_app_kt.ViewModelFactory
import com.rud.weather_app_kt.data.model.WeatherEntry
import com.rud.weather_app_kt.hideKeyboard
import com.rud.weather_app_kt.ui.weather_list.recyclerview.WeatherListAdapter
import kotlinx.android.synthetic.main.screen_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class WeatherListFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    lateinit var viewModel: WeatherListViewModel

    lateinit var weatherAdapter: WeatherListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherListViewModel::class.java)

        btn_add.setOnClickListener {
            viewModel.addWeatherEntry(et_location_name.text.toString(),
                onError = {
                    Toast.makeText(activity, "This city does not exist", Toast.LENGTH_SHORT)
                })

            et_location_name.setText("")
            hideKeyboard()
        }

        // init recyclerview
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            weatherAdapter = WeatherListAdapter()
            weatherAdapter.setOnClickListener { weatherEntryClicked(it) }
            adapter = weatherAdapter
        }

        viewModel.fetchData()

        // observe on the data
        viewModel.weatherList.observe(this, Observer { data ->
            Timber.d("changes observed, len=${data.size}")
            weatherAdapter.setData(data)
        })
    }

    private fun weatherEntryClicked(weatherEntry: WeatherEntry) {
        viewModel.removeWeatherEntry(weatherEntry)
    }
}