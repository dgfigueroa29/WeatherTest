@file:Suppress("BlockingMethodInNonBlockingContext")

package com.boa.data.datasource.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.boa.data.datasource.LocationDataSource

class LocationGeocoderDataSource(private val context: Context) : LocationDataSource {
    override suspend fun getFromLocation(latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context)
        val matches: List<Address> = geoCoder.getFromLocation(latitude, longitude, 1)
        val bestMatch = matches.firstOrNull()
        var location = ""

        if (bestMatch != null) {
            location = "${bestMatch.locality}, ${bestMatch.adminArea}"
            println("BestMatch CountryCode: " + bestMatch.countryCode)
            println("BestMatch Country: " + bestMatch.countryName)
            println("BestMatch Locality: " + bestMatch.locality)
            println("BestMatch SubLocality: " + bestMatch.subLocality)
            println("BestMatch AdminArea: " + bestMatch.adminArea)
            println("BestMatch SubAdminArea: " + bestMatch.subAdminArea)
            println("BestMatch Thoroughfare: " + bestMatch.thoroughfare)
            println("BestMatch SubThoroughfare: " + bestMatch.subThoroughfare)
            println("BestMatch Premises: " + bestMatch.premises)
            println("BestMatch PostalCode: " + bestMatch.postalCode)
            println("BestMatch Phone: " + bestMatch.phone)
            println("BestMatch MaxAddressLineIndex: " + bestMatch.maxAddressLineIndex)
            println("BestMatch Longitude: " + bestMatch.longitude)
            println("BestMatch Latitude: " + bestMatch.latitude)
            println("BestMatch FeatureName: " + bestMatch.featureName)
            println("BestMatch Extras: " + bestMatch.extras)
            println("BestMatch Lines: " + bestMatch.getAddressLine(0))
        }

        return location
    }
}