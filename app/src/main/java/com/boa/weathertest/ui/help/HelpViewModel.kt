package com.boa.weathertest.ui.help

import com.boa.domain.base.BaseException
import com.boa.weathertest.base.BaseViewModel
import com.boa.weathertest.util.HELP_WEB

class HelpViewModel : BaseViewModel<HelpViewStatus>() {
    override fun getInitialViewStatus(): HelpViewStatus = HelpViewStatus()

    fun initialize() {
        resourceViewStatus.value = HelpViewStatus(HELP_WEB)
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