package com.boa.domain.model

data class WeatherModel(
    val minTemp: Double = 0.0,
    val maxTemp: Double = 0.0,
    val pressure: Long = 0L,
    val humidity: Int = 0,
    val clouds: Int = 0,
    val windSpeed: Double = 0.0,
    val windDeg: Int = 0,
    val main: String = "",
    val description: String = ""
)