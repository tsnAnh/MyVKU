/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.koin

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.R
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

    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestIdToken(androidContext().getString(R.string.server_client_id))
            .requestProfile()
            .requestEmail()
            .build()
    }

    single {
        GoogleSignIn.getClient(androidContext(), get())
    }

    single {
        WorkManager.getInstance(androidContext())
    }
}