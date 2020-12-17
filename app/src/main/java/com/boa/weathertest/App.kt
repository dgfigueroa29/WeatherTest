package com.boa.weathertest

import androidx.multidex.MultiDexApplication
import com.boa.data.di.dataModule
import com.boa.domain.di.domainModule
import com.boa.weathertest.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

@Suppress("unused")
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            koin.loadModules(listOf(appModule, domainModule, dataModule))
            koin.createRootScope()
        }
    }
}