package com.boa.weathertest.ui.setting

import com.boa.domain.base.BaseException
import com.boa.domain.base.BaseStatusObserver
import com.boa.domain.model.UnitType
import com.boa.domain.usecase.GetUnitsUseCase
import com.boa.domain.usecase.SaveUnitsUseCase
import com.boa.weathertest.base.BaseViewModel

class SettingViewModel(
    private val getUnitsUseCase: GetUnitsUseCase,
    private val saveUnitsUseCase: SaveUnitsUseCase
) : BaseViewModel<SettingViewStatus>() {
    private var selectedUnit = ""

    override fun getInitialViewStatus(): SettingViewStatus = SettingViewStatus()

    fun initialize() {
        val viewStatus = getInitialViewStatus()
        BaseStatusObserver(
            resourceViewStatus,
            getUnitsUseCase.execute(null),
            {
                viewStatus.isReady = true
                selectedUnit = it ?: UnitType.METRIC.text
                viewStatus.currentUnits = selectedUnit
                resourceViewStatus.value = viewStatus
            },
            this::onError,
            this::onLoading
        )
    }

    fun setUnits(value: String) {
        if (value != selectedUnit) {
            selectedUnit = value
            val viewStatus = getInitialViewStatus()
            BaseStatusObserver(
                resourceViewStatus,
                saveUnitsUseCase.execute(SaveUnitsUseCase.Param(value)),
                {
                    viewStatus.isComplete = true
                    resourceViewStatus.value = viewStatus
                },
                this::onError,
                this::onLoading
            )
        }
    }

    override fun onError(exception: BaseException?) {
        val viewStatus = getInitialViewStatus()
        viewStatus.isError = true
        viewStatus.errorMessage = exception?.message ?: ""
        resourceViewStatus.value = viewStatus
    }

    override fun onLoading(progress: Int) {
        val viewStatus = getInitialViewStatus()
        viewStatus.isLoading = progress > 100
        resourceViewStatus.value = viewStatus
    }
}