/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.repository
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Transformations
//import androidx.lifecycle.liveData
//import dev.tsnanh.vku.domain.database.VKUDatabase
//import dev.tsnanh.vku.database.asDomainModel
//import dev.tsnanh.vku.domain.entities.*
//import dev.tsnanh.vku.domain.network.VKUServiceApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import org.koin.java.KoinJavaComponent.inject
//import retrofit2.HttpException
//import timber.log.Timber
//import java.net.SocketTimeoutException
//
//class VKURepository {
//    private val database: VKUDatabase by inject(VKUDatabase::class.java)
//    private val
//    val news: LiveData<List<News>> =
//        Transformations.map(database.dao.getAllNews()) {
//            it.asDomainModel()
//        }
//
//    fun getAllForums() = liveData(Dispatchers.IO) {
//        emit(Resource.Loading())
//        withContext(Dispatchers.IO) {
//            try {
//                emit(Resource.Success(VKUServiceApi.network.getAllSubForums().asDomainModel()))
//            } catch (e: SocketTimeoutException) {
//                emit(Resource.Error("Connection Timed Out", emptyList<Forum>()))
//            } catch (e2: HttpException) {
//                emit(Resource.Error("Cannot connect to server!", emptyList<Forum>()))
//            } catch (t: Throwable) {
//                emit(Resource.Error("Something went wrong!", emptyList<Forum>()))
//                Timber.d(t)
//            }
//        }
//    }
//
//    fun getReplies(threadId: String, page: Int) = liveData(Dispatchers.IO) {
//        emit(Resource.Loading<ReplyContainer>())
//        withContext(Dispatchers.IO) {
//            try {
//                emit(
//                    Resource.Success(
//
//                    )
//                )
//            } catch (e: SocketTimeoutException) {
//                emit(Resource.Error<ReplyContainer>("Connection Timed Out"))
//            } catch (e2: HttpException) {
//                emit(Resource.Error<ReplyContainer>("Cannot connect to server!"))
//            } catch (t: Throwable) {
//                emit(Resource.Error<PostContainer>("Something went wrong!"))
//            }
//        }
//    }
//
//    fun getThreadById(threadId: String) = liveData(Dispatchers.IO) {
//        emit(Resource.Loading())
//        withContext(Dispatchers.IO) {
//            try {
//                emit(
//                    Resource.Success(
//                        VKUServiceApi.network.getThreadById(threadId).asDomainModel()
//                    )
//                )
//            } catch (e: SocketTimeoutException) {
//                emit(Resource.Error<ForumThread>("Connection Timed Out"))
//            } catch (e2: HttpException) {
//                emit(Resource.Error<ForumThread>("Cannot connect to server!"))
//            } catch (t: Throwable) {
//                emit(Resource.Error<ForumThread>("Something went wrong!"))
//            }
//        }
//    }
//
//    fun getThreadsInForum(forumId: String) = liveData(Dispatchers.IO) {
//        withContext(Dispatchers.IO) {
//            try {
//                emit(VKUServiceApi.network.getThreadsInForum(forumId).asDomainModel())
//            } catch (e: Exception) {
//                Timber.e(e)
//            }
//        }
//    }
//
//    fun getForumById(forumId: String) = liveData(Dispatchers.IO) {
//        emit(VKUServiceApi.network.getForumById(forumId).asDomainModel())
//    }
//
//    suspend fun refreshNews() {
//        withContext(Dispatchers.IO) {
//            try {
//                val news = VKUServiceApi.network.getLatestNews()
//                database.dao.insertAllNews(*news.asDatabaseModel())
//            } catch (e: SocketTimeoutException) {
//                Timber.e("SocketTimeout")
//            } catch (t: Throwable) {
//                Timber.e(t)
//            }
//        }
//    }
//
//    fun getReplyById(quotedPostId: String) = liveData(Dispatchers.IO) {
//        emit(Resource.Loading<Reply>())
//        try {
//            emit(Resource.Success(VKUServiceApi.network.getReplyById(quotedPostId).asDomainModel()))
//        } catch (e: SocketTimeoutException) {
//            emit(Resource.Error<Reply>("Connection Timed Out", null))
//        } catch (e2: HttpException) {
//            emit(Resource.Error<Reply>("Cannot connect to server!", null))
//        } catch (t: Throwable) {
//            emit(Resource.Error<Reply>("Something went wrong!", null))
//        }
//    }
//}