package dev.tsnanh.vku.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.database.asDomainModel
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.News
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDatabaseModel
import dev.tsnanh.vku.network.asDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

class VKURepository(private val database: VKUDatabase) {

    val news: LiveData<List<News>> =
        Transformations.map(database.dao.getAllNews()) {
            it.asDomainModel()
        }

    val forums = liveData(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            emit(VKUServiceApi.network.getAllSubForums().asDomainModel())
        }
    }

    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            val news = VKUServiceApi.network.getLatestNews()
            database.dao.insertAllNews(*news.asDatabaseModel())
        }
    }
}