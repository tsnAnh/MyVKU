/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseNews::class], exportSchema = false, version = 1)
abstract class VKUDatabase : RoomDatabase() {
    abstract val dao: VKUDao
}