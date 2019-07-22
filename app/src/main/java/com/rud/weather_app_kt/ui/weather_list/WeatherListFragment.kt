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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.screen_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import kotlin.random.Random

class WeatherListFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    lateinit var viewModel: WeatherListViewModel

    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherListViewModel::class.java)

        btn_add_random.setOnClickListener {
            addWeatherEntry(Weather("Test ${Random.nextInt()}", 123f))
        }

        disposable.add(
            viewModel.getAllWeatherEntries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d(it.size.toString())
                    redraw(it)
                }, {
                    Timber.e(it)
                })
        )
    }

    private fun redraw(data: List<Weather>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = WeatherListAdapter(data) {
                weatherEntryClicked(it)
            }
        }
    }

    private fun weatherEntryClicked(weather: Weather) {
        removeWeatherEntry(weather)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun addWeatherEntry(weather: Weather) {
        disposable.add(
            viewModel.insertWeatherEntry(weather)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("add success")
                }, {
                    Timber.e(it)
                })
        )
    }

    private fun removeWeatherEntry(weather: Weather) {
        disposable.add(
            viewModel.removeWeatherEntry(weather)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("remove success")
                }, {
                    Timber.e(it)
                })
        )
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }
}