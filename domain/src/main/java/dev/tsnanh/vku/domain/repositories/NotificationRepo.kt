package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

interface NotificationRepo {
    fun getNotifications(idToken: String): Flow<Resource<List<Notification>>>
}

class NotificationRepoImpl : NotificationRepo {
    private val dao by inject(VKUDao::class.java)
    override fun getNotifications(idToken: String): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(VKUServiceApi.network.getNotifications(idToken)))
        } catch (e: Exception) {
            emit(ErrorHandler.handleError(e))
        }
    }
}