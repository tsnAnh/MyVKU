package dev.tsnanh.vku

import android.app.Application
import dev.tsnanh.vku.koin.vkuModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VKUApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VKUApplication)
            modules(vkuModule)
        }
    }
}