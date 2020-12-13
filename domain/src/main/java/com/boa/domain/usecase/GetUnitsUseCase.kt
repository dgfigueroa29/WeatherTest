package com.boa.domain.usecase

import com.boa.domain.base.BaseUseCase
import com.boa.domain.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineScope

class GetUnitsUseCase(
    scope: CoroutineScope,
    private val preferenceRepository: PreferenceRepository
) : BaseUseCase<String, Any?>(scope) {
    override suspend fun getData(param: Any?): String =
        preferenceRepository.getUnits()
}