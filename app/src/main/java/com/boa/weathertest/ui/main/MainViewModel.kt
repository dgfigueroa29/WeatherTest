package com.boa.weathertest.ui.main

import com.boa.domain.base.BaseException
import com.boa.weathertest.base.BaseViewModel

class MainViewModel : BaseViewModel<MainViewStatus>() {
    override fun getInitialViewStatus(): MainViewStatus = MainViewStatus()
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