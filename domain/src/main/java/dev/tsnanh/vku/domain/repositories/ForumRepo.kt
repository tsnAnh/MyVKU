package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.NetworkForum
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ForumRepo {
    fun getForums(): Flow<List<NetworkForum>>
}

class ForumRepoImpl @Inject constructor() : ForumRepo {
    override fun getForums(): Flow<List<NetworkForum>> {
        return flow {
            emit(VKUServiceApi.network.getForums())
        }
    }
}