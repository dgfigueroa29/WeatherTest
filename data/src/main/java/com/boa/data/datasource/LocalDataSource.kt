package com.boa.data.datasource

import com.boa.data.entity.CityEntity

interface LocalDataSource {
    suspend fun getAll(): List<CityEntity>
    suspend fun getByText(text: String): List<CityEntity>
    suspend fun getCurrentLocation(): CityEntity
    suspend fun getSelected(): List<CityEntity>
    suspend fun save(entity: CityEntity): Boolean
}