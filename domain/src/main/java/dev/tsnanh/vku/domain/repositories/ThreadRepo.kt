package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.domain.entities.Resource
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
}

class ThreadRepoImpl : ThreadRepo {
    override fun getThreads(forumId: String): Flow<Resource<List<NetworkForumThreadCustom>>> =
        flow {
            emit(Resource.Loading())
            delay(200)
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
}