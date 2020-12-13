package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineScope

class SaveUnitsUseCase(
    scope: CoroutineScope,
    private val preferenceRepository: PreferenceRepository
) : BaseUseCase<Any?, SaveUnitsUseCase.Param>(scope) {
    override suspend fun getData(param: Param): Any =
        preferenceRepository.saveUnits(param.value)

    data class Param(val value: String)
}