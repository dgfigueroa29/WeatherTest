package com.boa.weathertest.ui.city

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.boa.domain.model.CityModel
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.base.OnSelectItem
import com.boa.weathertest.util.*
import kotlinx.android.synthetic.main.city_fragment.*
import kotlinx.android.synthetic.main.view_header.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class CityFragment : BaseFragment<CityViewStatus, CityViewModel>(), OnSelectItem<CityModel> {
    private var selectedItem = CityModel()
    private lateinit var listAdapter: ListAdapter<CityModel>

    override fun initViewModel(): CityViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.city_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        viewHeaderToolbar?.setNavigationIcon(R.drawable.ic_back)
        viewHeaderToolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        viewHeaderTitle.text = getString(R.string.cities)
        val contextRef = WeakReference(requireContext().applicationContext)
        cityFragmentList?.build(contextRef)
        listAdapter = ListAdapter(contextRef)
        cityFragmentList?.adapter = listAdapter
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
                requireActivity().findNavController(R.id.cityFragmentRoot)
                    .navigate(
                        R.id.navigation_action_city_to_home,
                        bundleOf(
                            ARGUMENT_LAT to selectedItem.latitude,
                            ARGUMENT_LON to selectedItem.longitude
                        )
                    )
            }

            else -> {
                hideLoading()
            }
        }
    }

    override fun onSelectItem(item: CityModel) {
        selectedItem = item
    }
}