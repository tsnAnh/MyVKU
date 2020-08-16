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
import dev.tsnanh.vku.domain.entities.Subject
import kotlinx.coroutines.flow.Flow

/**
 * Database Access Object for VKUDatabase
 * @author tsnAnh
 */
@Dao
interface VKUDao {
    /**
     * Get all news cached from database
     * @return List<News>
     */
    @Query("SELECT * FROM news ORDER BY updatedDate DESC")
    fun getAllNews(): Flow<List<News>>

    /**
     * Insert all news get from REST API
     * @param news News
     */
    @Insert(entity = News::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNews(vararg news: News)

    /**
     * Insert all subjects to database
     * @param subjects Subjects
     */
    @Insert(entity = Subject::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSubjects(vararg subjects: Subject)

    /**
     * Retrieve all subjects cached in database
     * @return LiveData<List<Subject>>
     */
    @Query("SELECT * FROM subjects ORDER BY lesson")
    fun getAllSubjectsLiveData(): LiveData<List<Subject>>

    /**
     * Retrieve all subjects cached in database
     * @return List<Subject>
     */
    @Query("SELECT * FROM subjects ORDER BY lesson")
    fun getAllSubjects(): List<Subject>
}