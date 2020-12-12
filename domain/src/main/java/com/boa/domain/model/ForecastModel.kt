package com.boa.domain.model

data class ForecastModel(
    val current: WeatherModel,
    val daily: List<WeatherModel>
)