package com.boa.weathertest.ui.city

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewGroupCompat
import androidx.recyclerview.widget.RecyclerView
import com.boa.domain.model.UnitType
import com.boa.domain.model.WeatherModel
import com.boa.domain.model.WindSpeed
import com.boa.weathertest.R
import com.bumptech.glide.Glide
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

class WeatherAdapter<T>(
    private val context: WeakReference<Context>
) :
    RecyclerView.Adapter<WeatherHolder>() {
    private var list = listOf<T>()
    var currentUnits: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val view = LayoutInflater.from(context.get()).inflate(
            R.layout.item_forecast,
            parent,
            false
        )
        ViewGroupCompat.setTransitionGroup(view as ViewGroup, true)
        return WeatherHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val item = list[position] as WeatherModel
        val windSpeed = if (currentUnits == UnitType.IMPERIAL.text) {
            WindSpeed.IMPERIAL.text
        } else {
            WindSpeed.STANDARD.text
        }

        holder.itemForecastTemperature.text =
            "${item.minTemp.roundToInt()}° / ${item.maxTemp.roundToInt()}°"
        holder.itemForecastDetail.text = "${item.main}, ${item.description}"
        holder.itemForecastHumidity.text = "${item.humidity}%"
        holder.itemForecastRain.text = "${item.clouds}%"
        holder.itemForecastWind.text = "${item.windSpeed} $windSpeed"
        holder.itemForecastHumidity.visibility = View.VISIBLE
        holder.itemForecastRain.visibility = View.VISIBLE
        holder.itemForecastWind.visibility = View.VISIBLE

        if (item.icon.isNotEmpty()) {
            context.get()?.let {
                Glide.with(it).load(item.icon).into(holder.itemForecastImage)
                holder.itemForecastImage.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<T>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}