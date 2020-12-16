package com.boa.weathertest.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.boa.domain.model.CityModel
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.util.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.map_fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MapFragment : BaseFragment<MapViewStatus, MapViewModel>(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener {
    private var googleMap: GoogleMap? = null
    private var currentPosition = LatLng(0.0, 0.0)
    private var myMarker: Marker? = null
    private var currentLocation = CityModel()

    override fun initViewModel(): MapViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.map_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        viewModel.getCurrentCity()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        mapBackButton?.setOnClickListener {
            onBackPressed()
        }
        mapSelectButton?.setOnClickListener {
            saveNewLocation()
        }
    }

    override fun onViewStatusUpdated(viewStatus: MapViewStatus) {
        when {
            viewStatus.isError && viewStatus.errorMessage.isNotEmpty() -> {
                hideLoading()
                requireContext().applicationContext.toast(viewStatus.errorMessage)
            }

            viewStatus.isError -> {
                hideLoading()
                requireContext().applicationContext.toast(getString(R.string.error))
            }

            viewStatus.isReady -> {
                currentLocation = viewStatus.currentLocation
            }

            viewStatus.isComplete -> {
                currentLocation = viewStatus.currentLocation
                updatePosition()
            }

            viewStatus.isFinish -> {
                hideLoading()
                requireContext().applicationContext.toast(getString(R.string.modification_ok))
                onBackPressed()
            }
        }
    }

    private fun onBackPressed() {
        requireActivity().findNavController(R.id.mapFragmentRoot)
            .navigate(R.id.navigation_action_map_to_home)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = false
            googleMap?.uiSettings?.isMapToolbarEnabled = false
            googleMap?.uiSettings?.isCompassEnabled = false
            googleMap?.uiSettings?.isIndoorLevelPickerEnabled = false
            googleMap?.setOnMapClickListener(this)
            val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val provider = locationManager.getBestProvider(Criteria(), false)
            val location = locationManager.getLastKnownLocation(provider ?: "")
            currentPosition = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
            viewModel.getCurrentLocation(currentPosition.latitude, currentPosition.longitude)
            currentLocation =
                currentLocation.copy(
                    latitude = currentPosition.latitude,
                    longitude = currentPosition.longitude,
                    selected = true
                )
            updatePosition()
            hideLoading()
        }
    }

    override fun onMapClick(latLng: LatLng?) {
        if (latLng != null) {
            viewModel.getCurrentLocation(latLng.latitude, latLng.longitude)
            currentPosition = latLng
            updatePosition()
            mapSelectButton?.visibility = VISIBLE
        }
    }

    private fun updatePosition() {
        if (myMarker != null) {
            myMarker!!.remove()
        } else {
            viewModel.updateCurrentCity(currentLocation)
        }

        myMarker = googleMap?.addMarker(
            MarkerOptions().position(currentPosition).title(currentLocation.name)
        )
        myMarker?.showInfoWindow()
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(currentPosition))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 8f))
    }

    private fun saveNewLocation() {
        showLoading()
        viewModel.saveCity(currentLocation.copy(id = 0, selected = true))
    }
}