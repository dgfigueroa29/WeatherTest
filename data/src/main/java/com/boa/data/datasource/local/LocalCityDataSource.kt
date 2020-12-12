package com.boa.data.datasource.local

import com.boa.data.datasource.LocalDataSource
import com.boa.data.datasource.local.db.AppDatabase
import com.boa.data.entity.CityEntity

class LocalCityDataSource(private val database: AppDatabase) : LocalDataSource {
    override suspend fun getAll(): List<CityEntity> = database.cityDao().getAll()

    override suspend fun getByText(text: String): List<CityEntity> =
        database.cityDao().getByText(text)

    override suspend fun getSelected(): List<CityEntity> = database.cityDao().getSelected()

    override suspend fun save(entity: CityEntity): Boolean = try {
        database.cityDao().update(entity)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}