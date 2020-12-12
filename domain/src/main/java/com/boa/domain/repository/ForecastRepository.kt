package com.boa.domain.repository

import com.boa.domain.model.ForecastModel

interface ForecastRepository {
    suspend fun getOneCall(lat: Double, lon: Double): ForecastModel
}