/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import dev.tsnanh.vku.utils.Constants
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class VKUApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var preferences: SharedPreferences
    @Inject lateinit var workerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()

        // Timber debug
        Timber.plant(Timber.DebugTree())

        // UI Mode
        preferences.registerOnSharedPreferenceChangeListener { pref, _ ->
            when (pref?.getString(getString(R.string.night_mode_key), Constants.MODE_SYSTEM)) {
                Constants.MODE_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Constants.MODE_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
        when (preferences.getString(getString(R.string.night_mode_key), Constants.MODE_SYSTEM)) {
            Constants.MODE_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Constants.MODE_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()


}