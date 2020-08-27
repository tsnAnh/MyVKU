package dev.tsnanh.vku.services

import android.app.NotificationManager
import androidx.core.content.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.utils.sendCloudMessageNotification

@AndroidEntryPoint
class FirebaseMessagingForumService : FirebaseMessagingService() {
    private val notificationManager by lazy {
        getSystemService<NotificationManager>()
    }
    override fun onNewToken(token: String) {
        // TODO: send token to server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        notificationManager?.sendCloudMessageNotification(applicationContext, message.data)
    }
}
