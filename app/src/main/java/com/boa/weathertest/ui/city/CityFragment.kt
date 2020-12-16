package com.boa.weathertest.ui.city

import android.os.Bundle
import android.view.View
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.util.*
import kotlinx.android.synthetic.main.city_fragment.*
import kotlinx.android.synthetic.main.view_header.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class CityFragment : BaseFragment<CityViewStatus, CityViewModel>() {
    private var cityName = ""
    private var latitude = 0.0
    private var longitude = 0.0

    override fun initViewModel(): CityViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.city_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        viewHeaderToolbar?.setNavigationIcon(R.drawable.ic_back)
        viewHeaderToolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        viewHeaderTitle?.text = getString(R.string.cities)
        cityName = receiveSafeString(ARGUMENT_CITY)
        latitude = receiveSafeDouble(ARGUMENT_LAT)
        longitude = receiveSafeDouble(ARGUMENT_LON)
        val contextRef = WeakReference(requireContext().applicationContext)
        cityFragmentName?.text = cityName
    }

    override fun onViewStatusUpdated(viewStatus: CityViewStatus) {
        when {
            viewStatus.isError && viewStatus.errorMessage.isNotEmpty() -> {
                hideLoading()
                requireContext().applicationContext.toast(viewStatus.errorMessage)
            }

            viewStatus.isError -> {
                hideLoading()
                requireContext().applicationContext.toast(getString(R.string.error))
            }

            viewStatus.isLoading -> {
                showLoading()
            }

            viewStatus.isReady -> {
            }

            else -> {
                hideLoading()
            }
        }
    }
}