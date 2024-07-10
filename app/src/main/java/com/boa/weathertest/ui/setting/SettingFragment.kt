package com.boa.weathertest.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boa.domain.model.UnitType
import com.boa.domain.util.toStringList
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.base.OnSelectItem
import com.boa.weathertest.util.build
import com.boa.weathertest.util.toast
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.ref.WeakReference

class SettingFragment : BaseFragment<SettingViewStatus, SettingViewModel>(),
    OnSelectItem<String> {
    private var binding: SettingFragmentBinding? = null
    override fun initViewModel(): SettingViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.setting_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding?.root as View?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        binding?.viewHeaderToolbar?.setNavigationIcon(R.drawable.ic_back)
        binding?.viewHeaderToolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        binding?.viewHeaderTitle?.text = getString(R.string.setting)
        viewModel.initialize()
    }

    override fun onViewStatusUpdated(viewStatus: SettingViewStatus) {
        when {
            viewStatus.isComplete -> {
                hideLoading()
                requireContext().applicationContext.toast(getString(R.string.modification_ok))
            }

            viewStatus.isError && viewStatus.errorMessage.isNotEmpty() -> {
                hideLoading()
                requireContext().applicationContext.toast(viewStatus.errorMessage)
            }

            viewStatus.isError -> {
                hideLoading()
                requireContext().applicationContext.toast(getString(R.string.error))
            }

            viewStatus.isReady -> {
                hideLoading()
                binding?.settingFragmentSpinner.build(
                    this,
                    WeakReference(requireContext().applicationContext),
                    viewStatus.currentUnits,
                    UnitType.values().asList().toStringList()
                )
            }

            viewStatus.isLoading -> {
                showLoading()
            }
        }
    }

    override fun onSelectItem(item: String) {
        viewModel.setUnits(item)
    }
}