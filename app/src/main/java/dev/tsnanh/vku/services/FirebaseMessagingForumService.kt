package dev.tsnanh.vku.services

import android.app.NotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.tsnanh.vku.utils.sendCloudMessageNotification
import org.koin.java.KoinJavaComponent.inject

class FirebaseMessagingForumService : FirebaseMessagingService() {
    private val notificationManager by inject(NotificationManager::class.java)
    override fun onNewToken(token: String) {
        // TODO: send token to server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        notificationManager.sendCloudMessageNotification(applicationContext, message.data)
    }
}
