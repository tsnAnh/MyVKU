/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.domain.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.tsnanh.myvku.domain.entities.News
import dev.tsnanh.myvku.domain.entities.Subject
import kotlinx.coroutines.flow.Flow

/**
 * Database Access Object for VKUDatabase
 * @author tsnAnh
 */
@Dao
interface VKUDao {
    /**
     * Get all news cached from database
     * @author tsnAnh
     * @return List<News>
     */
    @Query("SELECT * FROM news ORDER BY updatedDate DESC")
    fun getAllNews(): Flow<List<News>>

    /**
     * Insert all news get from REST API
     * @author tsnAnh
     * @param news News
     */
    @Insert(entity = News::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNews(vararg news: News)

    /**
     * Insert all subjects to database
     * @author tsnAnh
     * @param subjects Subjects
     */
    @Insert(entity = Subject::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSubjects(vararg subjects: Subject)

    /**
     * Retrieve all subjects cached in database
     * @author tsnAnh
     * @return LiveData<List<Subject>>
     */
    @Query("SELECT * FROM subjects ORDER BY lesson")
    fun getAllSubjectsLiveData(): LiveData<List<Subject>>

    /**
     * Retrieve all subjects cached in database
     * @author tsnAnh
     * @return List<Subject>
     */
    @Query("SELECT * FROM subjects ORDER BY lesson")
    fun getAllSubjects(): Flow<List<Subject>>

    /**
     * Retrieve all subject with specified day of week
     * @author tsnAnh
     * @param dayOfWeek String
     * @return Flow<List<Subject>>
     */
    @Query("SELECT * FROM subjects WHERE dayOfWeek = :dayOfWeek")
    fun getTimetableWithFilter(dayOfWeek: String): Flow<List<Subject>>

    @Query("SELECT * FROM news ORDER BY createdDate DESC")
    suspend fun getLatestNews(): News
//
//    @Insert(entity = Teacher::class, onConflict = OnConflictStrategy.REPLACE)
//    fun insertAllTeachers(vararg teacher: Teacher)
}