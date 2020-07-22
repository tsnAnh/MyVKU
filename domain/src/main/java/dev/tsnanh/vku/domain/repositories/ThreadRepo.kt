package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.*
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ThreadRepo {
    fun getThreads(forumId: String): Flow<Resource<List<NetworkForumThreadCustom>>>
    suspend fun createThread(
        idToken: String,
        thread: ForumThread,
        forumId: String
    ): ForumThread

    fun updateThreadTitle(
        idToken: String,
        threadId: String,
        body: UpdateThreadBody
    ): Flow<Resource<NetworkForumThread>>

    suspend fun deleteThread(idToken: String, threadId: String): String
}

class ThreadRepoImpl : ThreadRepo {
    override fun getThreads(forumId: String): Flow<Resource<List<NetworkForumThreadCustom>>> =
        flow {
            emit(Resource.Loading())
            delay(100)
            try {
                emit(Resource.Success(VKUServiceApi.network.getThreads(forumId)))
            } catch (e: Exception) {
                emit(ErrorHandler.handleError(e))
            }
        }

    @Throws(Exception::class)
    override suspend fun createThread(
        idToken: String,
        thread: ForumThread,
        forumId: String
    ) = try {
        VKUServiceApi.network.createThread(idToken, thread, forumId)
    } catch (e: Exception) {
        throw e
    }

    override suspend fun deleteThread(idToken: String, threadId: String): String {
        return try {
            VKUServiceApi.network.deleteThread(idToken, threadId)
        } catch (e: Exception) {
            "error"
        }
    }

    override fun updateThreadTitle(
        idToken: String,
        threadId: String,
        body: UpdateThreadBody
    ): Flow<Resource<NetworkForumThread>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(VKUServiceApi.network.editThread(idToken, threadId, body)))
            } catch (e: Exception) {
                emit(ErrorHandler.handleError(e))
            }
        }
}