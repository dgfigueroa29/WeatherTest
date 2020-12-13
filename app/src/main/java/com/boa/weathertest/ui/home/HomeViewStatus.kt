package com.boa.weathertest.ui.home

import com.boa.domain.model.CityModel
import com.boa.weathertest.base.BaseViewStatus

class HomeViewStatus(
    var cityList: List<CityModel> = listOf(),
    var suggestedCities: List<CityModel> = listOf()
) : BaseViewStatus()