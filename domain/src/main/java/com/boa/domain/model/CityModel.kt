package com.boa.domain.model

data class CityModel(
    val id: Int = 0,
    val name: String = "",
    val state: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val selected: Boolean = false
)