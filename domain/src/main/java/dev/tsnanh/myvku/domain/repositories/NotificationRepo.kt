package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.entities.Notification
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NotificationRepo {
    fun getNotifications(idToken: String): Flow<State<List<Notification>>>
}

class NotificationRepoImpl @Inject constructor(): NotificationRepo {
    override fun getNotifications(idToken: String): Flow<State<List<Notification>>> = flow {
        emit(State.Loading())
        try {
            emit(State.Success(VKUServiceApi.network.getNotifications(idToken)))
        } catch (e: Exception) {
            emit(State.Error<List<Notification>>(e))
        }
    }
}