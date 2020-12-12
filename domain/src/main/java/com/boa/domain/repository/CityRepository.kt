package com.boa.domain.repository

import com.boa.domain.model.CityModel

interface CityRepository {
    suspend fun getAll(): List<CityModel>
    suspend fun getByText(text: String): List<CityModel>
    suspend fun getSelected(): List<CityModel>
    suspend fun save(model: CityModel): Boolean
}