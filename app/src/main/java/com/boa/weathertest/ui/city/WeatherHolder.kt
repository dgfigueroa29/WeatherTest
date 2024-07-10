package com.boa.weathertest.ui.city

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boa.weathertest.R

class WeatherHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemForecastTemperature: TextView = view.findViewById(R.id.itemForecastTemperature)
    val itemForecastImage: ImageView = view.findViewById(R.id.itemForecastImage)
    val itemForecastDetail: TextView = view.findViewById(R.id.itemForecastDetail)
    val itemForecastHumidity: TextView = view.findViewById(R.id.itemForecastHumidity)
    val itemForecastRain: TextView = view.findViewById(R.id.itemForecastRain)
    val itemForecastWind: TextView = view.findViewById(R.id.itemForecastWind)
}