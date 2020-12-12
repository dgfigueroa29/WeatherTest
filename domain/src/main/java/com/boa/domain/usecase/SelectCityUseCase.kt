package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.model.CityModel
import com.boa.domain.repository.CityRepository
import kotlinx.coroutines.CoroutineScope

class SelectCityUseCase(
    scope: CoroutineScope,
    private val cityRepository: CityRepository
) : BaseUseCase<Boolean, SelectCityUseCase.Param>(scope) {
    override suspend fun getData(param: Param): Boolean =
        cityRepository.save(param.city)

    data class Param(val city: CityModel)
}