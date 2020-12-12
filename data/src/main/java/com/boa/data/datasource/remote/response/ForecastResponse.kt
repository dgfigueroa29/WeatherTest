package com.boa.data.datasource.remote.response

data class ForecastResponse(
    val temp: TempResponse,
    val pressure: Long = 0L,
    val humidity: Int = 0,
    val clouds: Int = 0,
    val wind_speed: Double = 0.0,
    val wind_deg: Int = 0,
    val weather: List<WeatherResponse>
)