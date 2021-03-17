/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.domain.entities.MakeupClass
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
    fun getAllNewsFlow(): Flow<List<News>>

    @Query("SELECT * FROM news ORDER BY updatedDate DESC LIMIT :limit")
    suspend fun getAllNewsWithLimit(limit: Int = 10): List<News>

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
     * @return List<Subject>
     */
    @Query("SELECT * FROM subjects ORDER BY className")
    fun getAllSubjectsFlow(): Flow<List<Subject>>

    @Query("SELECT * FROM subjects ORDER BY className")
    fun getAllSubjects(): List<Subject>

    @Query("SELECT * FROM news ORDER BY updatedDate DESC")
    suspend fun getLatestNews(): News
//
//    @Insert(entity = Teacher::class, onConflict = OnConflictStrategy.REPLACE)
//    fun insertAllTeachers(vararg teacher: Teacher)
    @Query("DELETE FROM subjects")
    suspend fun deleteAllSubjects()

    @Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @Query("SELECT * FROM absence ORDER BY dateNotice DESC, className ASC")
    fun getAllAbsencesFlow(): Flow<List<Absence>>

    @Query("SELECT * FROM absence ORDER BY dateNotice DESC, className ASC LIMIT :limit")
    fun getAllAbsencesWithLimit(limit: Int = 10): List<Absence>

    @Query("DELETE FROM absence")
    suspend fun deleteAllAbsences()

    @Insert(entity = Absence::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAbsences(vararg absences: Absence)

    @Query("SELECT * FROM makeupclass ORDER BY dateMakeUp DESC")
    fun getAllMakeupClassesFlow(): Flow<List<MakeupClass>>

    @Query("SELECT * FROM makeupclass ORDER BY dateMakeUp DESC LIMIT :limit")
    fun getAllMakeupClassesWithLimit(limit: Int = 10): List<MakeupClass>

    @Query("DELETE FROM makeupclass")
    suspend fun deleteAllMakeupClasses()

    @Insert(entity = MakeupClass::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMakeupClasses(vararg makeupClasses: MakeupClass)
}