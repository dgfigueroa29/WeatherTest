package com.boa.weathertest.ui.city

import com.boa.domain.base.BaseException
import com.boa.domain.usecase.GetForecastUseCase
import com.boa.weathertest.base.BaseViewModel

class CityViewModel(
    private val getForecastUseCase: GetForecastUseCase
) :
    BaseViewModel<CityViewStatus>() {
    override fun getInitialViewStatus(): CityViewStatus = CityViewStatus()

    override fun onError(exception: BaseException?) {
        val viewStatus = getInitialViewStatus()
        viewStatus.isError = true
        viewStatus.errorMessage = exception?.message ?: ""
        resourceViewStatus.value = viewStatus
    }

    override fun onLoading(progress: Int) {
        val viewStatus = getInitialViewStatus()
        viewStatus.isLoading = progress > 100
        resourceViewStatus.value = viewStatus
    }
}