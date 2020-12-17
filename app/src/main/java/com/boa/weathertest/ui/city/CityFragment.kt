package com.boa.weathertest.ui.city

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.transition.TransitionManager
import com.boa.domain.model.WeatherModel
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.util.*
import com.boa.weathertest.view.Stagger
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.city_fragment.*
import kotlinx.android.synthetic.main.view_header.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class CityFragment : BaseFragment<CityViewStatus, CityViewModel>() {
    private lateinit var listAdapter: WeatherAdapter<WeatherModel>
    private var cityName = ""
    private var latitude = 0.0
    private var longitude = 0.0

    override fun initViewModel(): CityViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.city_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        val contextRef = WeakReference(requireContext().applicationContext)
        cityFragmentList?.build(contextRef)
        listAdapter = WeatherAdapter(contextRef)
        cityFragmentList?.adapter = listAdapter
        viewHeaderToolbar?.setNavigationIcon(R.drawable.ic_back)
        viewHeaderToolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        viewHeaderTitle?.text = getString(R.string.cities)
        cityName = receiveSafeString(ARGUMENT_CITY)
        latitude = receiveSafeDouble(ARGUMENT_LAT)
        longitude = receiveSafeDouble(ARGUMENT_LON)
        cityFragmentName?.text = cityName
        viewModel.initialize()
    }

    override fun onViewStatusUpdated(viewStatus: CityViewStatus) {
        when {
            viewStatus.currentUnits.isNotEmpty() -> {
                viewModel.getForecast(latitude, longitude)
            }

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
                cityFragmentTemperature?.text = viewStatus.currentTemp
                cityFragmentTempUnit?.text = viewStatus.currentUnitTemp
                cityFragmentDetail?.text = viewStatus.currentDetail
                cityFragmentHumidity?.text = viewStatus.currentHumidity
                cityFragmentRain?.text = viewStatus.currentRain
                cityFragmentWind?.text = viewStatus.currentWind
                cityFragmentHumidity?.visibility = VISIBLE
                cityFragmentRain?.visibility = VISIBLE
                cityFragmentWind?.visibility = VISIBLE

                if (viewStatus.currentIcon.isNotEmpty()) {
                    Glide.with(requireActivity()).load(viewStatus.currentIcon)
                        .into(cityFragmentImage)
                    cityFragmentImage?.visibility = VISIBLE
                }

                if (viewStatus.daily.isNotEmpty()) {
                    TransitionManager.beginDelayedTransition(cityFragmentList, Stagger())
                    listAdapter.setData(viewStatus.daily)
                }

                hideLoading()
            }
        }
    }
}