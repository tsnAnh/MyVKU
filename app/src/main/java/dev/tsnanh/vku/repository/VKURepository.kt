package dev.tsnanh.vku.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.database.asDomainModel
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.News
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VKURepository(private val database: VKUDatabase) {

    val news: LiveData<List<News>> =
        Transformations.map(database.dao.getAllNews()) {
            it.asDomainModel()
        }

    val forums: LiveData<List<Forum>> =
        Transformations.map(database.dao.getAllForums()) {
            it.asDomainModel()
        }

    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            val news = VKUServiceApi.network.getLatestNews()
            database.dao.insertAllNews(*news.asDatabaseModel())
        }
    }

    suspend fun refreshForums() {
        withContext(Dispatchers.IO) {
            database.dao.deleteAllForums()
            val forums = VKUServiceApi.network.getAllSubForums()
            database.dao.insertForums(*forums.asDatabaseModel())
        }
    }
}