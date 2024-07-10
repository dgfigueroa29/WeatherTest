package com.boa.weathertest.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.transition.TransitionManager
import com.boa.domain.model.CityModel
import com.boa.domain.util.toStringList
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.base.OnRemoveItem
import com.boa.weathertest.base.OnSelectItem
import com.boa.weathertest.databinding.HomeFragmentBinding
import com.boa.weathertest.util.ARGUMENT_CITY
import com.boa.weathertest.util.ARGUMENT_LAT
import com.boa.weathertest.util.ARGUMENT_LON
import com.boa.weathertest.util.ListAdapter
import com.boa.weathertest.util.PERMISSION_MAP_CODE
import com.boa.weathertest.util.PERMISSION_STORAGE_CODE
import com.boa.weathertest.util.build
import com.boa.weathertest.util.hideKeyboard
import com.boa.weathertest.util.toast
import com.boa.weathertest.view.Stagger
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class HomeFragment : BaseFragment<HomeViewStatus, HomeViewModel>(), OnSelectItem<CityModel>,
    OnRemoveItem<CityModel> {
    private var binding: HomeFragmentBinding? = null
    private var cities = listOf<CityModel>()
    private lateinit var listAdapter: ListAdapter<CityModel>
    private var searchEditText: AutoCompleteTextView? = null
    private var goToMap = false

    override fun initViewModel(): HomeViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.home_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        binding?.homeFragmentSearch?.searchCardEditText?.hideKeyboard()
        binding?.homeFragmentHeader?.viewHeaderToolbar?.inflateMenu(R.menu.menu)
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_STORAGE_CODE
        )
        val contextRef = WeakReference(requireContext().applicationContext)
        binding?.homeFragmentList?.build(contextRef)
        listAdapter = ListAdapter(contextRef, this, this)
        binding?.homeFragmentList?.adapter = listAdapter
        val mapItem =
            binding?.homeFragmentHeader?.viewHeaderToolbar?.menu?.findItem(R.id.menu_map_action)
        val settingItem =
            binding?.homeFragmentHeader?.viewHeaderToolbar?.menu?.findItem(R.id.menu_setting_action)
        val helpItem =
            binding?.homeFragmentHeader?.viewHeaderToolbar?.menu?.findItem(R.id.menu_help_action)
        searchEditText = binding?.homeFragmentSearch?.getInput()
        searchEditText?.setSelection(searchEditText?.text?.length ?: 0)
        searchEditText?.setHint(R.string.search)
        searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchEditText?.hideKeyboard()
                showLoading()
                viewModel.getSuggestions(binding?.homeFragmentSearch?.searchCardEditText?.text.toString())
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
            //binding?.homeFragmentSearch?.searchCardClear?.performClick()
            goToMap = true
            goToMap()
            true
        }
        settingItem?.setOnMenuItemClickListener {
            searchEditText?.hideKeyboard()
            //binding?.homeFragmentSearch?.searchCardClear?.performClick()
            requireActivity().findNavController(R.id.homeFragmentRoot)
                .navigate(R.id.navigation_action_home_to_setting)
            true
        }
        helpItem?.setOnMenuItemClickListener {
            searchEditText?.hideKeyboard()
            //binding?.homeFragmentSearch?.searchCardClear?.performClick()
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
            binding?.homeFragmentSearch?.searchCardEditText?.setAdapter(
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
            binding?.homeFragmentList?.let {
                TransitionManager.beginDelayedTransition(
                    it,
                    Stagger()
                )
            }
            listAdapter.setData(viewStatus.cityList)

            if (viewStatus.cityList.isNotEmpty()) {
                binding?.homeFragmentEmptyText?.visibility = GONE
            } else {
                binding?.homeFragmentEmptyText?.visibility = VISIBLE
            }

            hideLoading()
        } else {
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
                PERMISSION_MAP_CODE
            )
        } else {
            if (goToMap) {
                goToMap = false
                requireActivity().findNavController(R.id.homeFragmentRoot)
                    .navigate(R.id.navigation_action_home_to_map)
            }
        }
        hideLoading()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        goToMap()
    }
}