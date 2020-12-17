package com.boa.data.mapper

import com.boa.data.datasource.remote.response.ApiResponse
import com.boa.domain.base.BaseMapper
import com.boa.domain.model.ForecastModel
import com.boa.domain.model.WeatherModel

class ForecastResponseToModelMapper : BaseMapper<ApiResponse, ForecastModel>() {
    override fun map(input: ApiResponse): ForecastModel {
        val current = WeatherModel(
            input.current.temp,
            input.current.temp,
            input.current.pressure,
            input.current.humidity,
            input.current.clouds,
            input.current.wind_speed,
            input.current.wind_deg,
            input.current.weather.firstOrNull()?.main ?: "",
            input.current.weather.firstOrNull()?.description ?: "",
            if (input.current.weather.firstOrNull()?.icon.isNullOrEmpty()) {
                ""
            } else {
                "http://openweathermap.org/img/wn/${input.current.weather.firstOrNull()?.icon}@2x.png"
            }
        )
        val daily: MutableList<WeatherModel> = mutableListOf()
        input.daily.takeIf { it.isNotEmpty() }?.forEach {
            daily.add(
                WeatherModel(
                    it.temp.min,
                    it.temp.max,
                    it.pressure,
                    it.humidity,
                    it.clouds,
                    it.wind_speed,
                    it.wind_deg,
                    it.weather.firstOrNull()?.main ?: "",
                    it.weather.firstOrNull()?.description ?: "",
                    if (input.current.weather.firstOrNull()?.icon.isNullOrEmpty()) {
                        ""
                    } else {
                        "http://openweathermap.org/img/wn/${input.current.weather.firstOrNull()?.icon}@2x.png"
                    }
                )
            )
        }

        return ForecastModel(
            input.lat,
            input.lon,
            current, daily
        )
    }
}