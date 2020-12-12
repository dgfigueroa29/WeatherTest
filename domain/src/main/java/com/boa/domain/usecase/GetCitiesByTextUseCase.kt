package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.model.CityModel
import com.boa.domain.repository.CityRepository
import kotlinx.coroutines.CoroutineScope

class GetCitiesByTextUseCase(
    scope: CoroutineScope,
    private val cityRepository: CityRepository
) : BaseUseCase<List<CityModel>, GetCitiesByTextUseCase.Param>(scope) {
    override suspend fun getData(param: Param): List<CityModel> =
        cityRepository.getByText(param.text)

    data class Param(val text: String)
}