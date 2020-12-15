package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.model.CityModel
import com.boa.domain.repository.CityRepository
import kotlinx.coroutines.CoroutineScope

class GetCurrentLocationUseCase(
    scope: CoroutineScope,
    private val cityRepository: CityRepository
) : BaseUseCase<CityModel, Any?>(scope) {
    override suspend fun getData(param: Any?): CityModel =
        cityRepository.getCurrentLocation()
}