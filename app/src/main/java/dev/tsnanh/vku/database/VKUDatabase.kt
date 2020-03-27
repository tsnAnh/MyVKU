package dev.tsnanh.vku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseNews::class, DatabaseForum::class], exportSchema = false, version = 1)
abstract class VKUDatabase : RoomDatabase() {
    abstract val dao: VKUDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: VKUDatabase

        fun getInstance(context: Context): VKUDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room
                        .databaseBuilder(context, VKUDatabase::class.java, "vku_database")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}