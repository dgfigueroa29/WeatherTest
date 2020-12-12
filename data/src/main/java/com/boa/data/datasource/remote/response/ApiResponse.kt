package com.boa.data.datasource.remote.response

data class ApiResponse(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val current: CurrentForecastResponse,
    val daily: List<ForecastResponse>
)