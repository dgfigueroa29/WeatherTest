package com.boa.data.di

import com.boa.data.datasource.LocalDataSource
import com.boa.data.datasource.RemoteDataSource
import com.boa.data.datasource.local.LocalCityDataSource
import com.boa.data.datasource.local.db.AppDatabase
import com.boa.data.datasource.remote.RemoteForecastDataSource
import com.boa.data.mapper.CityEntityToModelMapper
import com.boa.data.mapper.CityModelToEntityMapper
import com.boa.data.mapper.ForecastResponseToModelMapper
import com.boa.data.repository.CityRepositoryImpl
import com.boa.data.repository.ForecastRepositoryImpl
import com.boa.domain.repository.CityRepository
import com.boa.domain.repository.ForecastRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<LocalDataSource> { LocalCityDataSource(get()) }
    single<RemoteDataSource> { RemoteForecastDataSource(get()) }

    single<CityRepository> { CityRepositoryImpl(get(), get(), get()) }
    single<ForecastRepository> { ForecastRepositoryImpl(get(), get()) }

    single { CityEntityToModelMapper() }
    single { CityModelToEntityMapper() }
    single { ForecastResponseToModelMapper() }

    single { AppDatabase.getAppDatabase(androidContext()) }
}