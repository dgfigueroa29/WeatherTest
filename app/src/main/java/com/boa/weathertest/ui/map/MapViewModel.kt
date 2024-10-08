package com.boa.weathertest.ui.map

import com.boa.domain.base.BaseException
import com.boa.domain.base.BaseStatusObserver
import com.boa.domain.model.CityModel
import com.boa.domain.usecase.GetCurrentCityUseCase
import com.boa.domain.usecase.GetCurrentLocationUseCase
import com.boa.domain.usecase.SaveCityUseCase
import com.boa.weathertest.base.BaseViewModel

class MapViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getCurrentCityUseCase: GetCurrentCityUseCase,
    private val saveCityUseCase: SaveCityUseCase,
) : BaseViewModel<MapViewStatus>() {
    override fun getInitialViewStatus(): MapViewStatus = MapViewStatus()

    fun getCurrentLocation(latitude: Double, longitude: Double) {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            getCurrentLocationUseCase.execute(GetCurrentLocationUseCase.Param(latitude, longitude)),
            {
                viewStatus.isComplete = true
                viewStatus.currentLocation =
                    it ?: CityModel(latitude = latitude, longitude = longitude, selected = true)
                resourceViewStatus.value = viewStatus
            },
            this::onError,
            this::onLoading
        )
    }

    fun getCurrentCity() {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            getCurrentCityUseCase.execute(null),
            {
                viewStatus.isReady = true
                viewStatus.currentLocation = it ?: CityModel()
                resourceViewStatus.value = viewStatus
            },
            this::onError,
            this::onLoading
        )
    }

    fun updateCurrentCity(city: CityModel) {
        if (city.name.isNotEmpty()) {
            val viewStatus = getInitialViewStatus()
            BaseStatusObserver(
                resourceViewStatus,
                saveCityUseCase.execute(SaveCityUseCase.Param(city)),
                {
                    viewStatus.currentLocation = city
                    resourceViewStatus.value = viewStatus
                },
                this::onError,
                this::onLoading
            )
        }
    }

    fun saveCity(city: CityModel) {
        if (city.name.isNotEmpty()) {
            val viewStatus = getInitialViewStatus()
            BaseStatusObserver(
                resourceViewStatus,
                saveCityUseCase.execute(SaveCityUseCase.Param(city)),
                {
                    viewStatus.isFinish = true
                    viewStatus.currentLocation = city
                    resourceViewStatus.value = viewStatus
                },
                this::onError,
                this::onLoading
            )
        }
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