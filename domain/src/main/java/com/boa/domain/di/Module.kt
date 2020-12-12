package com.boa.domain.di

import com.boa.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {
    factory { GetCitiesUseCase(get(), get()) }
    factory { GetCitiesByTextUseCase(get(), get()) }
    factory { GetCitiesSelectedUseCase(get(), get()) }
    factory { GetForecastUseCase(get(), get()) }
    factory { SelectCityUseCase(get(), get()) }

    single { CoroutineScope(Dispatchers.IO) }
}