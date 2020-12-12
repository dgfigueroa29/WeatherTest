package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.model.CityModel
import com.boa.domain.repository.CityRepository
import kotlinx.coroutines.CoroutineScope

class GetCitiesSelectedUseCase(
    scope: CoroutineScope,
    private val cityRepository: CityRepository
) : BaseUseCase<List<CityModel>, Any?>(scope) {
    override suspend fun getData(param: Any?): List<CityModel> =
        cityRepository.getSelected()
}