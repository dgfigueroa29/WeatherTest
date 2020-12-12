package com.boa.data.util

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val APP_ID = "?appid=c6e381d8c7ff98f0fee43775817cf6ad&units"
const val DEFAULT_EXCLUDE = "&exclude=hourly,alerts,minutely"
const val LAT_PARAM = "&lat="
const val LON_PARAM = "&lat="
const val FORECAST = "${BASE_URL}onecall${APP_ID}${DEFAULT_EXCLUDE}&units="