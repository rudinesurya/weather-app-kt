package com.rud.weather_app_kt.ui.weather_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rud.weather_app_kt.R
import com.rud.weather_app_kt.ViewModelFactory
import com.rud.weather_app_kt.data.model.Weather
import com.rud.weather_app_kt.ui.utils.ScopedFragment
import com.rud.weather_app_kt.ui.weather_list.recyclerview.WeatherListAdapter
import kotlinx.android.synthetic.main.screen_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class WeatherListFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    lateinit var viewModel: WeatherListViewModel

    private val dummyData = mutableListOf(
        Weather("London", 12.0f),
        Weather("Dublin", 15.0f)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherListViewModel::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = WeatherListAdapter(dummyData) {
                weatherEntryClicked(it)
            }
        }
    }

    private fun weatherEntryClicked(weather: Weather) {
        dummyData.remove(weather)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}