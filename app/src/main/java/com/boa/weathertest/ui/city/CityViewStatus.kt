package com.boa.weathertest.ui.city

import com.boa.domain.model.ForecastModel
import com.boa.weathertest.base.BaseViewStatus

class CityViewStatus(var forecastModel: ForecastModel = ForecastModel()) :
    BaseViewStatus()