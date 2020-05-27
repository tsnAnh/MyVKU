/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dev.tsnanh.vku.koin.vkuModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class VKUApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Timber debug
        Timber.plant(Timber.DebugTree())

        //Koin DI
        startKoin {
            androidLogger()
            androidContext(this@VKUApplication)
            modules(vkuModule)
        }

        // UI Mode
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (preferences.getString(getString(R.string.night_mode_key), "system")) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}