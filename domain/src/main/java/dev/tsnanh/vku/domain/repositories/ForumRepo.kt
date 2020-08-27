package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.Forum
import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ForumRepo {
    fun getForums(): Flow<Resource<List<NetworkCustomForum>>>
    fun getForumById(forumId: String): Flow<Resource<Forum>>
}

class ForumRepoImpl @Inject constructor() : ForumRepo {
    override fun getForums(): Flow<Resource<List<NetworkCustomForum>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(VKUServiceApi.network.getForums()))
        } catch (e: Exception) {
            emit(ErrorHandler.handleError<List<NetworkCustomForum>>(e))
        }
    }

    override fun getForumById(forumId: String): Flow<Resource<Forum>> {
        return flow {
            emit(Resource.Loading<Forum>())
            try {
                emit(Resource.Success(VKUServiceApi.network.getForumById(forumId)))
            } catch (e: Exception) {
                emit(ErrorHandler.handleError<Forum>(e))
            }
        }
    }
}