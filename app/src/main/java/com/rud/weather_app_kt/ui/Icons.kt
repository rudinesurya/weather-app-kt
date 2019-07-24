package com.rud.weather_app_kt.ui

import com.rud.weather_app_kt.R

const val icon_sunny = R.drawable.ic_sun
const val icon_rainy = R.drawable.ic_rain
const val icon_cloudy = R.drawable.ic_cloudy

val background_images =
    listOf(R.drawable.overlay_1, R.drawable.overlay_2, R.drawable.overlay_3, R.drawable.overlay_4)

fun getConditionIcon(code: Int): Int {
    return when {
        code <= 1000 -> icon_sunny
        code <= 1030 -> icon_cloudy
        else -> icon_rainy
    }
}

fun getBackgroundImage(position: Int): Int {
    return background_images[position % 4]
}