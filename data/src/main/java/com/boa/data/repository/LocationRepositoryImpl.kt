package com.boa.data.repository

import com.boa.data.datasource.LocalDataSource
import com.boa.data.datasource.LocationDataSource
import com.boa.data.mapper.CityEntityToModelMapper
import com.boa.domain.model.CityModel
import com.boa.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource,
    private val localDataSource: LocalDataSource,
    private val cityEntityToModelMapper: CityEntityToModelMapper
) :
    LocationRepository {
    override suspend fun getFromLocation(latitude: Double, longitude: Double): CityModel {
        val city = cityEntityToModelMapper.map(localDataSource.getCurrentLocation())
        return city.copy(
            name = locationDataSource.getFromLocation(latitude, longitude),
            latitude = latitude,
            longitude = longitude,
            selected = true
        )
    }
}