package com.boa.data.datasource

import com.boa.data.datasource.remote.response.ApiResponse

interface RemoteDataSource {
    suspend fun getDetail(unitType: String, lat: Double, lon: Double): ApiResponse
}