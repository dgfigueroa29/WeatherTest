package com.boa.data.repository

import com.boa.data.datasource.LocalDataSource
import com.boa.data.mapper.CityEntityToModelMapper
import com.boa.data.mapper.CityModelToEntityMapper
import com.boa.domain.model.CityModel
import com.boa.domain.repository.CityRepository

class CityRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val cityEntityToModelMapper: CityEntityToModelMapper,
    private val cityModelToEntityMapper: CityModelToEntityMapper
) : CityRepository {
    override suspend fun getAll(): List<CityModel> =
        cityEntityToModelMapper.mapAll(localDataSource.getAll())

    override suspend fun getByText(text: String): List<CityModel> =
        cityEntityToModelMapper.mapAll(localDataSource.getByText(text))

    override suspend fun getSelected(): List<CityModel> =
        cityEntityToModelMapper.mapAll(localDataSource.getSelected())

    override suspend fun save(model: CityModel): Boolean =
        localDataSource.save(cityModelToEntityMapper.map(model))
}