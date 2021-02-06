package dev.tsnanh.myvku.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.tsnanh.myvku.R

@Module
@InstallIn(ApplicationComponent::class)
object AppModuleDependencies {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideGoogleSignInOptions(
        @ApplicationContext context: Context
    ): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestIdToken(context.getString(R.string.server_client_id))
            .requestProfile()
            .requestEmail()
            .build()

    @Provides
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context,
        googleSignInOptions: GoogleSignInOptions
    ): GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ) = WorkManager.getInstance(context)
}