package com.boa.weathertest

import android.app.Application
import com.boa.data.di.dataModule
import com.boa.domain.di.domainModule
import com.boa.weathertest.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            koin.loadModules(listOf(appModule, domainModule, dataModule))
            koin.createRootScope()
        }
    }
}