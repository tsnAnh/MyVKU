package dev.tsnanh.myvku.domain.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.database.VKUDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
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

    @Provides
    @Singleton
    fun provideVKUDao(
        database: VKUDatabase
    ): VKUDao = database.dao
}