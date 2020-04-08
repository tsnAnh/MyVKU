package dev.tsnanh.vku.koin

import androidx.room.Room
import dev.tsnanh.vku.R
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.repository.VKURepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val vkuModule = module {
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

    single {
        get<VKUDatabase>().dao
    }

    single(createdAtStart = true) {
        VKURepository(get())
    }
}