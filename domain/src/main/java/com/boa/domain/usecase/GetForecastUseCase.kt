package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.model.ForecastModel
import com.boa.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineScope

class GetForecastUseCase(
    scope: CoroutineScope,
    private val forecastRepository: ForecastRepository
) : BaseUseCase<ForecastModel, GetForecastUseCase.Param>(scope) {
    override suspend fun getData(param: Param): ForecastModel =
        forecastRepository.getOneCall(param.unitType, param.lat, param.lon)

    data class Param(val unitType: String, val lat: Double, val lon: Double)
}