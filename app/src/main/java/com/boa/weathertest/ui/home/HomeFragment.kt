package com.boa.weathertest.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.boa.domain.model.CityModel
import com.boa.domain.util.toStringList
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.base.OnRemoveItem
import com.boa.weathertest.base.OnSelectItem
import com.boa.weathertest.util.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.search_card.*
import kotlinx.android.synthetic.main.view_header.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class HomeFragment : BaseFragment<HomeViewStatus, HomeViewModel>(), OnSelectItem<CityModel>,
    OnRemoveItem<CityModel> {
    private var cities = listOf<CityModel>()
    private lateinit var listAdapter: ListAdapter<CityModel>
    private var searchEditText: AutoCompleteTextView? = null

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
        searchCardEditText?.hideKeyboard()
        viewHeaderToolbar?.inflateMenu(R.menu.menu)
        val contextRef = WeakReference(requireContext().applicationContext)
        homeFragmentList?.build(contextRef)
        listAdapter = ListAdapter(contextRef, this, this)
        homeFragmentList?.adapter = listAdapter
        val mapItem = viewHeaderToolbar?.menu?.findItem(R.id.menu_map_action)
        val settingItem = viewHeaderToolbar?.menu?.findItem(R.id.menu_setting_action)
        val helpItem = viewHeaderToolbar?.menu?.findItem(R.id.menu_help_action)
        searchEditText = homeFragmentSearch.getInput()
        searchEditText?.setSelection(searchEditText?.text?.length ?: 0)
        searchEditText?.setHint(R.string.search)
        searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchEditText?.hideKeyboard()
                showLoading()
                viewModel.getSuggestions(searchCardEditText?.text.toString())
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
        searchEditText?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            showLoading()
            viewModel.saveCity(position)
        }
        mapItem?.setOnMenuItemClickListener {
            searchEditText?.hideKeyboard()
            searchCardClear?.performClick()
            goToMap()
            true
        }
        settingItem?.setOnMenuItemClickListener {
            searchEditText?.hideKeyboard()
            searchCardClear?.performClick()
            requireActivity().findNavController(R.id.homeFragmentRoot)
                .navigate(R.id.navigation_action_home_to_setting)
            true
        }
        helpItem?.setOnMenuItemClickListener {
            searchEditText?.hideKeyboard()
            searchCardClear?.performClick()
            requireActivity().findNavController(R.id.homeFragmentRoot)
                .navigate(R.id.navigation_action_home_to_help)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSelected()
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

            viewStatus.isComplete -> {
                viewModel.getSelected()
                hideLoading()
                requireContext().applicationContext.toast(getString(R.string.modification_ok))
            }
        }

        if (viewStatus.suggestedCities.isNotEmpty()) {
            cities = viewStatus.suggestedCities
            searchCardEditText?.setAdapter(
                ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    viewStatus.suggestedCities.toStringList(false)
                )
            )

            hideLoading()
            searchEditText?.showDropDown()
        }

        if (viewStatus.cityList.isNotEmpty()) {
            listAdapter.setData(viewStatus.cityList)

            if (viewStatus.cityList.isNotEmpty()) {
                homeFragmentEmptyText?.visibility = GONE
            } else {
                homeFragmentEmptyText?.visibility = VISIBLE
            }

            hideLoading()
        }
    }

    override fun onSelectItem(item: CityModel) {
        requireActivity().findNavController(R.id.homeFragmentRoot)
            .navigate(
                R.id.navigation_action_home_to_city,
                bundleOf(
                    ARGUMENT_CITY to item.name,
                    ARGUMENT_LAT to "${item.latitude}",
                    ARGUMENT_LON to "${item.longitude}"
                )
            )
    }

    override fun onRemoveItem(item: CityModel) {
        viewModel.removeCity(item)
    }

    private fun goToMap() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_CODE
            )
        } else {
            requireActivity().findNavController(R.id.homeFragmentRoot)
                .navigate(R.id.navigation_action_home_to_map)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        goToMap()
    }
}