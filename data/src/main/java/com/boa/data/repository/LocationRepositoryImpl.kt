package com.boa.data.repository

import com.boa.data.datasource.LocationDataSource
import com.boa.domain.model.CityModel
import com.boa.domain.repository.LocationRepository

class LocationRepositoryImpl(private val locationDataSource: LocationDataSource) :
    LocationRepository {
    override suspend fun getFromLocation(latitude: Double, longitude: Double): CityModel =
        CityModel(
            name = locationDataSource.getFromLocation(latitude, longitude),
            lat = latitude,
            lon = longitude,
            selected = true
        )
}