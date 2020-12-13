package com.boa.weathertest.ui.map

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.help_fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MapFragment : BaseFragment<MapViewStatus, MapViewModel>() {
    override fun initViewModel(): MapViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.map_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        helpBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onViewStatusUpdated(viewStatus: MapViewStatus) {
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        hideLoading()
    }

    private fun onBackPressed() {
        requireActivity().findNavController(R.id.helpFragmentRoot)
            .navigate(R.id.navigation_action_help_to_home)
    }
}