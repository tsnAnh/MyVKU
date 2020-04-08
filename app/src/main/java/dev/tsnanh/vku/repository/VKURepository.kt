package dev.tsnanh.vku.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.database.asDomainModel
import dev.tsnanh.vku.domain.Forum
import dev.tsnanh.vku.domain.News
import dev.tsnanh.vku.domain.Resource
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDatabaseModel
import dev.tsnanh.vku.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException

class VKURepository(private val database: VKUDatabase) {

    val news: LiveData<List<News>> =
        Transformations.map(database.dao.getAllNews()) {
            it.asDomainModel()
        }

    val forums = liveData(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            try {
                emit(Resource.Success(VKUServiceApi.network.getAllSubForums().asDomainModel()))
            } catch (e: SocketTimeoutException) {
                emit(Resource.Error("Connection Timed Out", emptyList<Forum>()))
            } catch (e2: HttpException) {
                emit(Resource.Error("Cannot connect to server!", emptyList<Forum>()))
            } catch (t: Throwable) {
                emit(Resource.Error("Something went wrong!", emptyList<Forum>()))
            }
        }
    }

    fun getThreadsInForum(forumId: String) = liveData(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            emit(VKUServiceApi.network.getThreadsInForum(forumId).asDomainModel())
        }
    }

    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            try {
                val news = VKUServiceApi.network.getLatestNews()
                database.dao.insertAllNews(*news.asDatabaseModel())
            } catch (e: SocketTimeoutException) {
                Timber.e("SocketTimeout")
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}