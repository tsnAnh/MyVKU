package dev.tsnanh.myvku.domain.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.database.VKUDatabase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideVKUDatabase(
        @ApplicationContext context: Context
    ): VKUDatabase = synchronized(context) {
        Room
            .databaseBuilder(
                context,
                VKUDatabase::class.java,
                "vku_database"
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideVKUDao(
        database: VKUDatabase
    ): VKUDao = database.dao
}