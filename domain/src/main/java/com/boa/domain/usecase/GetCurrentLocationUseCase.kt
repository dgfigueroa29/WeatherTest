package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.model.CityModel
import com.boa.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineScope

class GetCurrentLocationUseCase(
    scope: CoroutineScope,
    private val locationRepository: LocationRepository
) : BaseUseCase<CityModel, GetCurrentLocationUseCase.Param>(scope) {
    override suspend fun getData(param: Param): CityModel =
        locationRepository.getFromLocation(param.latitude, param.longitude)

    data class Param(val latitude: Double, val longitude: Double)
}