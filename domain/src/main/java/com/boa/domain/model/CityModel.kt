package com.boa.domain.model

data class CityModel(
    val id: Int = 0,
    val name: String = "",
    val state: String = "",
    val country: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val selected: Boolean = false
)