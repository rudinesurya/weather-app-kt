package com.rud.weather_app_kt.ui.weather_list.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rud.weather_app_kt.R
import com.rud.weather_app_kt.data.model.WeatherEntry
import kotlinx.android.synthetic.main.weather_list_item.view.*

class WeatherListAdapter :
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    private val data = mutableListOf<WeatherEntry>()
    private lateinit var clickListener: (WeatherEntry) -> Unit

    class WeatherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(weatherEntry: WeatherEntry, clickListener: (WeatherEntry) -> Unit) = with(itemView) {
            tv_city_name.text = weatherEntry.city
            tv_temperature.text = weatherEntry.temperature.toString()
            tv_local_time.text = weatherEntry.localTime
            tv_condition.text = weatherEntry.condition
            setOnClickListener { clickListener(weatherEntry) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        WeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_list_item, parent, false))

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) = holder.bind(data[position], clickListener)

    override fun getItemCount(): Int = data.size

    fun setData(data: List<WeatherEntry>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnClickListener(clickListener: (WeatherEntry) -> Unit) {
        this.clickListener = clickListener
    }
}