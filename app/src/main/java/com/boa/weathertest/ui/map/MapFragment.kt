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
import com.boa.domain.base.BaseError
import com.boa.domain.base.BaseException
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
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

    override fun initViewModel(): MapViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.map_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        mapBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onViewStatusUpdated(viewStatus: MapViewStatus) {
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
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onError(BaseException(BaseError(requireActivity().getString(R.string.permissions))))
            return
        }

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
        updatePosition()
        hideLoading()
    }

    override fun onMapClick(latLng: LatLng?) {
        if (latLng != null) {
            currentPosition = latLng
            updatePosition()
            mapSelectButton.visibility = VISIBLE
        }
    }

    private fun updatePosition() {
        if (myMarker != null) {
            myMarker!!.remove()
        }

        myMarker = googleMap?.addMarker(MarkerOptions().position(currentPosition))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(currentPosition))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 12f))
    }
}