package dev.tsnanh.myvku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.myvku.domain.entities.Notification
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.repositories.NotificationRepo
import javax.inject.Inject

interface RetrieveNotificationsUseCase {
    fun getNotifications(idToken: String): LiveData<State<List<Notification>>>
}

class RetrieveNotificationsUseCaseImpl @Inject constructor(
    private val notificationRepo: NotificationRepo
) : RetrieveNotificationsUseCase {
    override fun getNotifications(idToken: String): LiveData<State<List<Notification>>> {
        return notificationRepo.getNotifications(idToken).asLiveData()
    }
}
