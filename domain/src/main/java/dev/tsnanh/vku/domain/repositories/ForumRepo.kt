package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ForumRepo {
    fun getForums(): Flow<Resource<List<NetworkCustomForum>>>
}

class ForumRepoImpl : ForumRepo {
    override fun getForums(): Flow<Resource<List<NetworkCustomForum>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(VKUServiceApi.network.getForums()))
            } catch (e: Exception) {
                emit(ErrorHandler.handleError(e))
            }
        }
}