package dev.tsnanh.vku.domain

import android.app.Application
import dev.tsnanh.vku.domain.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VKUDomainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VKUDomainApplication)
            modules(module)
        }
    }
}