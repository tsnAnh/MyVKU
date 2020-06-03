/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.koin

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Dependency Injection module for Koin
 * @author tsnAnh
 */
val vkuModule = module {
    // Notification Manager
    single {
        ContextCompat.getSystemService(
            androidContext(),
            NotificationManager::class.java
        ) as NotificationManager
    }

    // Shared Preferences
    single {
        PreferenceManager.getDefaultSharedPreferences(androidContext())
    }
}