@file:Suppress("BlockingMethodInNonBlockingContext")

package com.boa.data.datasource.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.boa.data.datasource.LocationDataSource

class LocationGeocoderDataSource(private val context: Context) : LocationDataSource {
    override suspend fun getFromLocation(latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context)
        val matches: List<Address?>? = geoCoder.getFromLocation(latitude, longitude, 1)
        val bestMatch = matches?.firstOrNull()
        var location = ""

        if (bestMatch != null) {
            location = if (bestMatch.locality.isNullOrEmpty()) {
                bestMatch.subAdminArea
            } else {
                bestMatch.locality
            }

            location += ", ${bestMatch.adminArea}"
        }

        return location
    }
}