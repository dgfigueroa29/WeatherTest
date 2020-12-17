package com.boa.weathertest.ui.city

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_forecast.view.*

class WeatherHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemForecastTemperature: TextView = view.itemForecastTemperature
    val itemForecastImage: ImageView = view.itemForecastImage
    val itemForecastDetail: TextView = view.itemForecastDetail
    val itemForecastHumidity: TextView = view.itemForecastHumidity
    val itemForecastRain: TextView = view.itemForecastRain
    val itemForecastWind: TextView = view.itemForecastWind
}