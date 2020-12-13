package com.boa.data.datasource.local

import android.content.Context
import com.boa.data.datasource.PreferenceDataSource
import com.boa.data.util.PREFERENCES
import com.boa.domain.model.UnitType

class PreferenceStringDataSource(private val context: Context) : PreferenceDataSource {
    override suspend fun getString(key: String): String =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            .getString(key, UnitType.METRIC.text) ?: UnitType.METRIC.text

    override suspend fun putString(key: String, value: String) {
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().putString(key, value)
            .apply()
    }
}