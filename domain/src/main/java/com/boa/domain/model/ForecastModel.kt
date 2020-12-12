package com.boa.domain.model

data class ForecastModel(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val current: WeatherModel,
    val daily: List<WeatherModel>
)