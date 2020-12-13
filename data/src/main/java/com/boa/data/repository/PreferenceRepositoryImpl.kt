package com.boa.data.repository

import com.boa.data.datasource.PreferenceDataSource
import com.boa.data.util.PREF_UNITS
import com.boa.domain.repository.PreferenceRepository

class PreferenceRepositoryImpl(private val preferenceDataSource: PreferenceDataSource) :
    PreferenceRepository {
    override suspend fun getUnits(): String = preferenceDataSource.getString(PREF_UNITS)

    override suspend fun saveUnits(value: String) {
        preferenceDataSource.putString(PREF_UNITS, value)
    }
}