package com.boa.data.repository

import com.boa.data.datasource.RemoteDataSource
import com.boa.data.mapper.ForecastResponseToModelMapper
import com.boa.domain.model.ForecastModel
import com.boa.domain.repository.ForecastRepository

class ForecastRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val forecastResponseToModelMapper: ForecastResponseToModelMapper
) : ForecastRepository {
    override suspend fun getOneCall(unitType: String, lat: Double, lon: Double): ForecastModel =
        forecastResponseToModelMapper.map(remoteDataSource.getDetail(unitType, lat, lon))
}