/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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
    @Insert(entity = DatabaseNews::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNews(vararg news: DatabaseNews)

    /**
     * Retrieve all news cached from database
     * @return LiveData<List<DatabaseNews>>
     */
    @Query("SELECT * FROM news ORDER BY date DESC")
    fun getAllNews(): LiveData<List<DatabaseNews>>

}