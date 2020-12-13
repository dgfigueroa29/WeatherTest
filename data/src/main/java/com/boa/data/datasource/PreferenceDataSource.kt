package com.boa.data.datasource

interface PreferenceDataSource {
    suspend fun getString(key: String): String
    suspend fun putString(key: String, value: String)
}