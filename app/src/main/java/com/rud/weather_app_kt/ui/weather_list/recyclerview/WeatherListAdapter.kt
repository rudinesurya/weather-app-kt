package com.rud.weather_app_kt.ui.weather_list.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rud.weather_app_kt.R
import com.rud.weather_app_kt.data.model.Weather
import kotlinx.android.synthetic.main.weather_list_item.view.*

class WeatherListAdapter(private val list: List<Weather>, val clickListener: (Weather) -> Unit) :
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(weather: Weather, clickListener: (Weather) -> Unit) = with(itemView) {
            tv_location_name.text = weather.locationName
            tv_temperature.text = weather.temperature.toString()
            setOnClickListener { clickListener(weather) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        WeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_list_item, parent, false))

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) = holder.bind(list[position], clickListener)

    override fun getItemCount(): Int = list.size
}