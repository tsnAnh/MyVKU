package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NotificationRepo {
    fun getNotifications(idToken: String): Flow<Resource<List<Notification>>>
}

class NotificationRepoImpl @Inject constructor(): NotificationRepo {
    override fun getNotifications(idToken: String): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(VKUServiceApi.network.getNotifications(idToken)))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}