package com.boa.domain.repository

import com.boa.domain.model.ForecastModel

interface ForecastRepository {
    suspend fun getOneCall(unitType: String, lat: Double, lon: Double): ForecastModel
}