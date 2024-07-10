package com.boa.data.datasource.remote.response

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val temp: TempResponse,
    val pressure: Long = 0L,
    val humidity: Int = 0,
    val clouds: Int = 0,
    @SerializedName("wind_speed")
    val windSpeed: Double = 0.0,
    @SerializedName("wind_deg")
    val windDeg: Int = 0,
    val weather: List<WeatherResponse>
)