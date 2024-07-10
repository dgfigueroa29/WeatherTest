package com.boa.weathertest.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.transition.TransitionManager
import com.boa.domain.model.WeatherModel
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.databinding.CityFragmentBinding
import com.boa.weathertest.util.ARGUMENT_CITY
import com.boa.weathertest.util.ARGUMENT_LAT
import com.boa.weathertest.util.ARGUMENT_LON
import com.boa.weathertest.util.build
import com.boa.weathertest.util.receiveSafeDouble
import com.boa.weathertest.util.receiveSafeString
import com.boa.weathertest.util.toast
import com.boa.weathertest.view.Stagger
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class CityFragment : BaseFragment<CityViewStatus, CityViewModel>() {
    private var binding: CityFragmentBinding? = null
    private var listAdapter: WeatherAdapter<WeatherModel>? = null
    private var cityName = ""
    private var latitude = 0.0
    private var longitude = 0.0

    override fun initViewModel(): CityViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.city_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CityFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        val contextRef = WeakReference(requireContext().applicationContext)
        binding?.cityFragmentList?.build(contextRef)
        listAdapter = WeatherAdapter(contextRef)
        binding?.cityFragmentList?.adapter = listAdapter
        binding?.cityFragmentHeader?.viewHeaderToolbar?.setNavigationIcon(R.drawable.ic_back)
        binding?.cityFragmentHeader?.viewHeaderToolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding?.cityFragmentHeader?.viewHeaderTitle?.text = getString(R.string.cities)
        cityName = receiveSafeString(ARGUMENT_CITY)
        latitude = receiveSafeDouble(ARGUMENT_LAT)
        longitude = receiveSafeDouble(ARGUMENT_LON)
        binding?.cityFragmentName?.text = cityName
        viewModel.initialize()
    }

    override fun onViewStatusUpdated(viewStatus: CityViewStatus) {
        when {
            viewStatus.currentUnits.isNotEmpty() -> {
                listAdapter?.currentUnits = viewStatus.currentUnits
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
                binding?.cityFragmentTemperature?.text = viewStatus.currentTemp
                binding?.cityFragmentTempUnit?.text = viewStatus.currentUnitTemp
                binding?.cityFragmentDetail?.text = viewStatus.currentDetail
                binding?.cityFragmentHumidity?.text = viewStatus.currentHumidity
                binding?.cityFragmentRain?.text = viewStatus.currentRain
                binding?.cityFragmentWind?.text = viewStatus.currentWind
                binding?.cityFragmentHumidity?.visibility = VISIBLE
                binding?.cityFragmentRain?.visibility = VISIBLE
                binding?.cityFragmentWind?.visibility = VISIBLE

                if (viewStatus.currentIcon.isNotEmpty()) {
                    binding?.cityFragmentImage?.let {
                        Glide.with(requireActivity()).load(viewStatus.currentIcon)
                            .into(it)
                    }
                    binding?.cityFragmentImage?.visibility = VISIBLE
                }

                if (viewStatus.daily.isNotEmpty()) {
                    binding?.cityFragmentList?.let {
                        TransitionManager.beginDelayedTransition(
                            it,
                            Stagger()
                        )
                    }
                    listAdapter?.setData(viewStatus.daily)
                }

                hideLoading()
            }
        }
    }
}