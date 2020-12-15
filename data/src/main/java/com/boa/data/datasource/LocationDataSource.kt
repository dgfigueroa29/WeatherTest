package com.boa.data.datasource

interface LocationDataSource {
    suspend fun getFromLocation(latitude: Double, longitude: Double): String
}