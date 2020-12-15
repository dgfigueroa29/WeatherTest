package com.boa.weathertest.ui.map

import com.boa.domain.model.CityModel
import com.boa.weathertest.base.BaseViewStatus

class MapViewStatus(
    var currentLocation: CityModel = CityModel(),
    var isFinish: Boolean = false
) : BaseViewStatus()