package com.boa.weathertest.ui.city

import com.boa.domain.model.WeatherModel
import com.boa.weathertest.base.BaseViewStatus

class CityViewStatus(
    var currentTemp: String = "",
    var currentDetail: String = "",
    var currentUnits: String = "",
    var currentUnitTemp: String = "",
    var currentHumidity: String = "",
    var currentRain: String = "",
    var currentWind: String = "",
    var currentIcon: String = "",
    var daily: List<WeatherModel> = listOf()
) :
    BaseViewStatus()