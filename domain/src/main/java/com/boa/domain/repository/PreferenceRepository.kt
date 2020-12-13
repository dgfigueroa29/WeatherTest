package com.boa.domain.repository

interface PreferenceRepository {
    suspend fun getUnits(): String
    suspend fun saveUnits(value: String)
}