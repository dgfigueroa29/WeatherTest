package com.boa.weathertest.ui.city

import com.boa.domain.base.BaseException
import com.boa.domain.base.BaseStatusObserver
import com.boa.domain.model.ForecastModel
import com.boa.domain.model.UnitTemp
import com.boa.domain.model.UnitType
import com.boa.domain.model.WindSpeed
import com.boa.domain.usecase.GetForecastUseCase
import com.boa.domain.usecase.GetUnitsUseCase
import com.boa.weathertest.base.BaseViewModel
import kotlin.math.roundToInt

class CityViewModel(
    private val getForecastUseCase: GetForecastUseCase,
    private val getUnitsUseCase: GetUnitsUseCase
) : BaseViewModel<CityViewStatus>() {
    private var selectedUnit: String = UnitType.METRIC.text

    override fun getInitialViewStatus(): CityViewStatus = CityViewStatus()

    fun initialize() {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            getUnitsUseCase.execute(null),
            {
                viewStatus.isReady = true
                selectedUnit = it ?: UnitType.METRIC.text
                viewStatus.currentUnits = selectedUnit
                resourceViewStatus.value = viewStatus
            },
            this::onError,
            this::onLoading
        )
    }

    fun getForecast(latitude: Double, longitude: Double) {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            getForecastUseCase.execute(GetForecastUseCase.Param(selectedUnit, latitude, longitude)),
            {
                val windSpeed = if (selectedUnit == UnitType.IMPERIAL.text) {
                    WindSpeed.IMPERIAL.text
                } else {
                    WindSpeed.STANDARD.text
                }

                val currentForecast = it ?: ForecastModel()
                viewStatus.daily = currentForecast.daily
                viewStatus.currentIcon = currentForecast.current.icon
                viewStatus.currentTemp = "${currentForecast.current.minTemp.roundToInt()}°"
                viewStatus.currentDetail =
                    "${currentForecast.current.main}, ${currentForecast.current.description}"
                viewStatus.currentHumidity = "${currentForecast.current.humidity}%"
                viewStatus.currentRain = "${currentForecast.current.clouds}%"
                viewStatus.currentWind = "${currentForecast.current.windSpeed} $windSpeed"
                viewStatus.currentUnitTemp = when (selectedUnit) {
                    UnitType.IMPERIAL.text -> {
                        UnitTemp.FAHRENHEIT.text.take(1)
                    }

                    UnitType.METRIC.text -> {
                        UnitTemp.CELSIUS.text.take(1)
                    }

                    else -> {
                        UnitTemp.KELVIN.text.take(1)
                    }
                }
                viewStatus.isReady = true
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