/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.domain.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.tsnanh.vku.domain.entities.News

/**
 * Database Access Object for VKUDatabase
 * @author tsnAnh
 */
@Dao
interface VKUDao {
    /**
     * Insert all news to database
     * @param news DatabaseNews
     */
    @Insert(entity = News::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNews(vararg news: News)

    /**
     * Retrieve all news cached from database
     * @return LiveData<List<DatabaseNews>>
     */
    @Query("SELECT * FROM news ORDER BY date DESC")
    fun getAllNews(): LiveData<List<News>>

}