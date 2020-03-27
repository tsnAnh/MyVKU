package dev.tsnanh.vku.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VKUDao {
    @Insert(entity = DatabaseNews::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNews(vararg news: DatabaseNews)

    @Query("SELECT * FROM news ORDER BY date DESC")
    fun getAllNews(): LiveData<List<DatabaseNews>>

    @Insert(entity = DatabaseForum::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertForums(vararg forums: DatabaseForum)

    @Query("DELETE FROM forums")
    fun deleteAllForums()

    @Query("SELECT * FROM forums ORDER BY last_updated_on ASC")
    fun getAllForums(): LiveData<List<DatabaseForum>>
}