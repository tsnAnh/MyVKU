package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.NotificationRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveNotificationsUseCase {
    fun getNotifications(idToken: String): LiveData<Resource<List<Notification>>>
}

class RetrieveNotificationsUseCaseImpl : RetrieveNotificationsUseCase {
    private val notificationRepo by inject(NotificationRepo::class.java)
    override fun getNotifications(idToken: String): LiveData<Resource<List<Notification>>> {
        return notificationRepo.getNotifications(idToken).asLiveData()
    }
}
