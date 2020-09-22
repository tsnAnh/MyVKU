package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UpdateThreadBody
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ThreadRepo {
    fun getThreads(forumId: String): Flow<Resource<List<NetworkForumThread>>>
    suspend fun createThread(
        idToken: String,
        thread: ForumThread,
        forumId: String,
    ): ForumThread

    fun updateThreadTitle(
        idToken: String,
        threadId: String,
        body: UpdateThreadBody,
    ): Flow<Resource<ForumThread>>

    fun deleteThread(idToken: String, threadId: String): Flow<Resource<String>>
}

class ThreadRepoImpl @Inject constructor() : ThreadRepo {
    override fun getThreads(forumId: String): Flow<Resource<List<NetworkForumThread>>> =
        flow {
            emit(Resource.Loading())
            delay(100)
            try {
                emit(Resource.Success(VKUServiceApi.network.getThreads(forumId)))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }

    @Throws(Exception::class)
    override suspend fun createThread(
        idToken: String,
        thread: ForumThread,
        forumId: String,
    ) = try {
        VKUServiceApi.network.createThread(idToken, thread, forumId)
    } catch (e: Exception) {
        throw e
    }

    override fun deleteThread(idToken: String, threadId: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(VKUServiceApi.network.deleteThread(idToken, threadId)))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }

    override fun updateThreadTitle(
        idToken: String,
        threadId: String,
        body: UpdateThreadBody,
    ): Flow<Resource<ForumThread>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(VKUServiceApi.network.editThread(idToken, threadId, body)))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
}