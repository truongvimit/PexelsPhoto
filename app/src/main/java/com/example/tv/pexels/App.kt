package com.example.tv.pexels

import android.app.Application
import com.example.tv.pexels.di.databaseModule
import com.example.tv.pexels.di.dispatcherModule
import com.example.tv.pexels.di.networkModule
import com.example.tv.pexels.di.repositoryModule
import com.example.tv.pexels.di.useCaseModule
import com.example.tv.pexels.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule,
                useCaseModule,
                dispatcherModule,
                repositoryModule,
                networkModule,
                databaseModule,
            )
        }
    }
}