package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.NetworkForum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ForumRepo {
    fun getForums(): Flow<Resource<List<NetworkForum>>>
}

class ForumRepoImpl @Inject constructor() : ForumRepo {
    override fun getForums(): Flow<Resource<List<NetworkForum>>> {
        return flow {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(VKUServiceApi.network.getForums()))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }
}
