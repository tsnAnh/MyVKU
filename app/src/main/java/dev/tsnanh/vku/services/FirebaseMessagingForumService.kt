package dev.tsnanh.vku.services

import android.app.NotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.tsnanh.vku.utils.sendCloudMessageNotification
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class FirebaseMessagingForumService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        // TODO: send token to server

    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notificationManager by inject(NotificationManager::class.java)
        notificationManager.sendCloudMessageNotification(applicationContext, message.data)
        Timber.d("${message.data}")
    }
}
