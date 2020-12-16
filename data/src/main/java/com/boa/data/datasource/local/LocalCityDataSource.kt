package com.boa.data.datasource.local

import com.boa.data.datasource.LocalDataSource
import com.boa.data.datasource.local.db.AppDatabase
import com.boa.data.entity.CityEntity
import com.boa.data.util.CURRENT_LOCATION
import java.util.*

class LocalCityDataSource(private val database: AppDatabase) : LocalDataSource {
    override suspend fun getAll(): List<CityEntity> = database.cityDao().getAll()

    override suspend fun getByText(text: String): List<CityEntity> =
        database.cityDao().getByText("%${text.toLowerCase(Locale.ROOT)}%")

    override suspend fun getCurrentLocation(): CityEntity =
        database.cityDao().getByText("%${CURRENT_LOCATION}%")[0]

    override suspend fun getSelected(): List<CityEntity> = database.cityDao().getSelected()

    override suspend fun save(entity: CityEntity): Boolean = try {
        database.cityDao().insert(entity)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}