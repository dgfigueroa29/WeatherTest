package com.boa.weathertest.ui.main

import com.boa.weathertest.R
import com.boa.weathertest.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity<MainViewStatus, MainViewModel>() {
    override fun initViewModel(): MainViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.main_activity

    override fun onViewStatusUpdated(viewStatus: MainViewStatus) {
    }
}