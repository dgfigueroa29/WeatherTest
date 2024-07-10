package com.boa.domain.di

import com.boa.domain.usecase.GetCitiesByTextUseCase
import com.boa.domain.usecase.GetCitiesSelectedUseCase
import com.boa.domain.usecase.GetCitiesUseCase
import com.boa.domain.usecase.GetCurrentCityUseCase
import com.boa.domain.usecase.GetCurrentLocationUseCase
import com.boa.domain.usecase.GetForecastUseCase
import com.boa.domain.usecase.GetUnitsUseCase
import com.boa.domain.usecase.SaveCityUseCase
import com.boa.domain.usecase.SaveUnitsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {
    factory { GetCitiesUseCase(get(), get()) }
    factory { GetCitiesByTextUseCase(get(), get()) }
    factory { GetCitiesSelectedUseCase(get(), get()) }
    factory { GetCurrentCityUseCase(get(), get()) }
    factory { GetCurrentLocationUseCase(get(), get()) }
    factory { GetForecastUseCase(get(), get()) }
    factory { GetUnitsUseCase(get(), get()) }
    factory { SaveUnitsUseCase(get(), get()) }
    factory { SaveCityUseCase(get(), get()) }

    single { CoroutineScope(Dispatchers.IO) }
}