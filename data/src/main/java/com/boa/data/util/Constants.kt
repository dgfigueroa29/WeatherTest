package com.boa.data.util

const val PREFERENCES = "APP_PREF"
const val PREF_UNITS = "units"
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val APP_ID = "?appid=c6e381d8c7ff98f0fee43775817cf6ad"
const val DEFAULT_EXCLUDE = "&exclude=hourly,alerts,minutely"
const val LAT_PARAM = "&lat="
const val LON_PARAM = "&lon="
const val FORECAST = "${BASE_URL}onecall${APP_ID}${DEFAULT_EXCLUDE}&units="
const val CURRENT_LOCATION = "current location"