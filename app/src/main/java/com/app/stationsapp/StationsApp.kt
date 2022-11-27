package com.app.stationsapp

import android.app.Application
import com.app.stationsapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StationsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StationsApp)
            modules(appModule)
        }
    }
}