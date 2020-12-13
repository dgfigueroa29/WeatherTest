package com.boa.weathertest.di

import com.boa.weathertest.ui.city.CityViewModel
import com.boa.weathertest.ui.help.HelpViewModel
import com.boa.weathertest.ui.home.HomeViewModel
import com.boa.weathertest.ui.main.MainViewModel
import com.boa.weathertest.ui.map.MapViewModel
import com.boa.weathertest.ui.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { CityViewModel(get()) }
    viewModel { HelpViewModel() }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MainViewModel() }
    viewModel { MapViewModel() }
    viewModel { SettingViewModel(get(), get()) }
}
