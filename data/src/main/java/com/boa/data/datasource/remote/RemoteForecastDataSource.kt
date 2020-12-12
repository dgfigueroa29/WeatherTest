package com.boa.data.datasource.remote

import com.boa.data.datasource.RemoteDataSource
import com.boa.data.datasource.remote.response.ApiResponse
import com.boa.data.util.FORECAST
import com.boa.data.util.LAT_PARAM
import com.boa.data.util.LON_PARAM

class RemoteForecastDataSource(
    private val appApi: AppApi
) : RemoteDataSource {
    override suspend fun getDetail(unitType: String, lat: Double, lon: Double): ApiResponse =
        appApi.getOneCall("${FORECAST}${unitType}${LAT_PARAM}${lat}${LON_PARAM}$lon")
}