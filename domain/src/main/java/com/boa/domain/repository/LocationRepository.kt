package com.boa.domain.repository

import com.boa.domain.model.CityModel

interface LocationRepository {
    suspend fun getFromLocation(latitude: Double, longitude: Double): CityModel
}