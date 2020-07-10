/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.entities.SubjectNotification

/**
 * Room Database abstract class for caching news
 * @author tsnAnh
 */
@Database(
    entities = [News::class, Subject::class, SubjectNotification::class],
    exportSchema = false,
    version = 1
)
abstract class VKUDatabase : RoomDatabase() {
    abstract val dao: VKUDao
}