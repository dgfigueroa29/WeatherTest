package com.boa.weathertest.ui.home

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.addCallback
import androidx.navigation.findNavController
import com.boa.domain.model.CityModel
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.base.OnSelectItem
import com.boa.weathertest.util.ListAdapter
import com.boa.weathertest.util.build
import com.boa.weathertest.util.toast
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.view_header.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class HomeFragment : BaseFragment<HomeViewStatus, HomeViewModel>(), OnSelectItem<String> {
    private var selectedAccount = ""
    private lateinit var listAdapter: ListAdapter<CityModel>

    override fun initViewModel(): HomeViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.home_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        viewHeaderToolbar.inflateMenu(R.menu.menu)
        val contextRef = WeakReference(requireContext().applicationContext)
        homeFragmentList.build(contextRef)
        listAdapter = ListAdapter(contextRef)
        homeFragmentList.adapter = listAdapter
        val mapItem = viewHeaderToolbar.menu.findItem(R.id.menu_map_action)
        val settingItem = viewHeaderToolbar.menu.findItem(R.id.menu_setting_action)
        val helpItem = viewHeaderToolbar.menu.findItem(R.id.menu_help_action)
        mapItem?.setOnMenuItemClickListener {
            requireActivity().findNavController(R.id.homeFragmentRoot)
                .navigate(R.id.navigation_action_home_to_map)
            true
        }
        settingItem?.setOnMenuItemClickListener {
            requireActivity().findNavController(R.id.homeFragmentRoot)
                .navigate(R.id.navigation_action_home_to_setting)
            true
        }
        helpItem?.setOnMenuItemClickListener {
            requireActivity().findNavController(R.id.homeFragmentRoot)
                .navigate(R.id.navigation_action_home_to_help)
            true
        }
    }

    override fun onViewStatusUpdated(viewStatus: HomeViewStatus) {
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
                hideLoading()
            }

            else -> {
                listAdapter.setData(viewStatus.cityList)

                if (viewStatus.cityList.isNotEmpty()) {
                    homeFragmentEmptyText.visibility = GONE
                } else {
                    homeFragmentEmptyText.visibility = VISIBLE
                }

                hideLoading()
            }
        }
    }

    override fun onSelectItem(item: String) {
        selectedAccount = item
    }
}