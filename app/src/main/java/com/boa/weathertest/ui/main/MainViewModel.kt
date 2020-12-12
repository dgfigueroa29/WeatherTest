package com.boa.weathertest.ui.main

import com.boa.domain.base.BaseException
import com.boa.weathertest.base.BaseViewModel

class MainViewModel : BaseViewModel<MainViewStatus>() {
    override fun getInitialViewStatus(): MainViewStatus = MainViewStatus()
    override fun onError(exception: BaseException?) {
    }

    override fun onLoading(progress: Int) {
    }
}