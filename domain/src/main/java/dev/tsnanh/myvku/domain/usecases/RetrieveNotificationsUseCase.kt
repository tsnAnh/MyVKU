package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.Notification
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.repositories.NotificationRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveNotificationsUseCase {
    fun getNotifications(idToken: String): Flow<State<List<Notification>>>
}

class RetrieveNotificationsUseCaseImpl @Inject constructor(
    private val notificationRepo: NotificationRepo
) : RetrieveNotificationsUseCase {
    override fun getNotifications(idToken: String): Flow<State<List<Notification>>> {
        return notificationRepo.getNotifications(idToken)
    }
}
