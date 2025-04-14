package com.id.newsapp

import android.app.Application
import com.id.data.dataModule
import com.id.domain.di.domainModule
import com.id.newsapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)

            val koinModules = listOf(
                appModule, domainModule, dataModule
            )


            modules(koinModules)
        }
    }
}
