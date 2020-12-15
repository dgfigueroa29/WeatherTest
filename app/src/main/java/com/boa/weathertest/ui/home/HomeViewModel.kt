package com.boa.weathertest.ui.home

import com.boa.domain.base.BaseException
import com.boa.domain.base.BaseStatusObserver
import com.boa.domain.model.CityModel
import com.boa.domain.usecase.GetCitiesSelectedUseCase
import com.boa.domain.usecase.GetCitiesUseCase
import com.boa.domain.usecase.SaveCityUseCase
import com.boa.weathertest.base.BaseViewModel

class HomeViewModel(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val getCitiesSelectedUseCase: GetCitiesSelectedUseCase,
    private val saveCityUseCase: SaveCityUseCase
) :
    BaseViewModel<HomeViewStatus>() {
    override fun getInitialViewStatus(): HomeViewStatus = HomeViewStatus()

    fun saveCity(city: CityModel) {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            saveCityUseCase.execute(SaveCityUseCase.Param(city)),
            {
                viewStatus.isComplete = true
                resourceViewStatus.value = viewStatus
            },
            this::onError,
            this::onLoading
        )
    }

    fun getSuggestions() {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            getCitiesUseCase.execute(null),
            {
                viewStatus.suggestedCities = it ?: listOf()
                resourceViewStatus.value = viewStatus
            },
            this::onError,
            this::onLoading
        )
    }

    fun getSelected() {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            getCitiesSelectedUseCase.execute(null),
            {
                viewStatus.cityList = it ?: listOf()
                resourceViewStatus.value = viewStatus
            },
            this::onError,
            this::onLoading
        )
    }

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