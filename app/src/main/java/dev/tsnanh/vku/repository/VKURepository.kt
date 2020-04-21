/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.database.asDomainModel
import dev.tsnanh.vku.domain.*
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

    fun getAllForums() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        withContext(Dispatchers.IO) {
            try {
                emit(Resource.Success(VKUServiceApi.network.getAllSubForums().asDomainModel()))
            } catch (e: SocketTimeoutException) {
                emit(Resource.Error("Connection Timed Out", emptyList<Forum>()))
            } catch (e2: HttpException) {
                emit(Resource.Error("Cannot connect to server!", emptyList<Forum>()))
            } catch (t: Throwable) {
                emit(Resource.Error("Something went wrong!", emptyList<Forum>()))
                Timber.d(t)
            }
        }
    }

    @SuppressWarnings("UNUSED")
    fun getReplies(threadId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        withContext(Dispatchers.IO) {
            try {
                emit(
                    Resource.Success(
                        VKUServiceApi.network.getRepliesInThread(threadId).asDomainModel()
                    )
                )
            } catch (e: SocketTimeoutException) {
                emit(Resource.Error("Connection Timed Out", emptyList<Post>()))
            } catch (e2: HttpException) {
                emit(Resource.Error("Cannot connect to server!", emptyList<Post>()))
            } catch (t: Throwable) {
                emit(Resource.Error("Something went wrong!", emptyList<Post>()))
            }
        }
    }

    fun getThreadById(threadId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        withContext(Dispatchers.IO) {
            try {
                emit(
                    Resource.Success(
                        VKUServiceApi.network.getThreadById(threadId).asDomainModel()
                    )
                )
            } catch (e: SocketTimeoutException) {
                emit(Resource.Error<ForumThread>("Connection Timed Out"))
            } catch (e2: HttpException) {
                emit(Resource.Error<ForumThread>("Cannot connect to server!"))
            } catch (t: Throwable) {
                emit(Resource.Error<ForumThread>("Something went wrong!"))
            }
        }
    }

    fun getThreadsInForum(forumId: String) = liveData(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            try {
                emit(VKUServiceApi.network.getThreadsInForum(forumId).asDomainModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun getForumById(forumId: String) = liveData(Dispatchers.IO) {
        emit(VKUServiceApi.network.getForumById(forumId).asDomainModel())
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

    fun getReplyById(quotedPostId: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading<Post>())
        try {
            emit(Resource.Success(VKUServiceApi.network.getReplyById(quotedPostId).asDomainModel()))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error<Post>("Connection Timed Out", null))
        } catch (e2: HttpException) {
            emit(Resource.Error<Post>("Cannot connect to server!", null))
        } catch (t: Throwable) {
            emit(Resource.Error<Post>("Something went wrong!", null))
        }
    }
}