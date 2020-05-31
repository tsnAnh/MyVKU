/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.koin

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.room.Room
import dev.tsnanh.vku.R
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.repository.VKURepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Dependency Injection module for Koin
 * @author tsnAnh
 */
val vkuModule = module {
    // Room SQLite
    single {
        synchronized(androidContext()) {
            Room
                .databaseBuilder(
                    androidContext(),
                    VKUDatabase::class.java,
                    androidContext().getString(R.string.vku_database)
                )
                .build()
        }
    }

    // DAO
    single {
        get<VKUDatabase>().dao
    }

    // Repository
    single(createdAtStart = true) {
        VKURepository()
    }

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