/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import dev.tsnanh.vku.R
import dev.tsnanh.vku.activities.MainActivity
import dev.tsnanh.vku.domain.entities.NotificationTitle.MESSAGE_LIKE
import dev.tsnanh.vku.domain.entities.NotificationTitle.MESSAGE_TO_ALL_SUBSCRIBERS
import dev.tsnanh.vku.domain.entities.NotificationTitle.MESSAGE_TO_OWNER
import dev.tsnanh.vku.domain.entities.NotificationTitle.MESSAGE_TO_OWNER_CUSTOM
import dev.tsnanh.vku.domain.entities.NotificationTitle.MESSAGE_TO_QUOTED_USER
import org.koin.java.KoinJavaComponent

/**
 * Create a new notification channel
 * @author tsnAnh
 * @param channelId String
 * @param channelName String
 */
fun createNotificationChannel(channelId: String, channelName: String) {
    val manager by KoinJavaComponent.inject(NotificationManager::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(false)
            enableLights(false)
            enableVibration(true)
        }

        manager.createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.sendSchoolReminderNotification(
    uid: Int,
    title: String,
    message: String,
    className: String?,
    applicationContext: Context
) {
    val pendingIntent = NavDeepLinkBuilder(applicationContext)
        .setComponentName(MainActivity::class.java)
        .setDestination(R.id.navigation_timetable)
        .setGraph(R.navigation.nav_graph)
        .setArguments(bundleOf("subject" to className))
        .createPendingIntent()
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.school_reminder_channel_id)
    ).apply {
        setContentTitle(title)
        setSmallIcon(R.mipmap.ic_launcher)
        setContentText(message)
        setContentIntent(pendingIntent)
        setAutoCancel(true)
        setStyle(NotificationCompat.BigTextStyle().bigText(message))
        setAutoCancel(true)
    }
    notify(uid, builder.build())
}

@Throws(IllegalArgumentException::class)
fun NotificationManager.sendCloudMessageNotification(
    applicationContext: Context,
    payload: Map<String, String>
) {
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.firebase_forum_notification_channel)
    ).apply {
        val title = when (payload["title"]) {
            MESSAGE_LIKE.value -> applicationContext.getString(R.string.text_message_like)
            MESSAGE_TO_OWNER.value -> applicationContext.getString(R.string.text_message_to_owner)
            MESSAGE_TO_OWNER_CUSTOM.value -> applicationContext.getString(R.string.text_message_to_owner_custom)
            MESSAGE_TO_QUOTED_USER.value -> applicationContext.getString(R.string.text_message_to_quoted_user)
            MESSAGE_TO_ALL_SUBSCRIBERS.value -> applicationContext.getString(R.string.text_message_to_all_subscribers)
            else -> throw IllegalArgumentException("WTF is this message???")
        }
        val fullContent = "${payload["userDisplayName"]} $title"
        setContentText(fullContent)
        setContentTitle("Notification from ${payload["userDisplayName"]}")
        setStyle(NotificationCompat.BigTextStyle().bigText(fullContent))
        setAutoCancel(true)
        setSmallIcon(R.mipmap.ic_launcher)
    }

    notify(2, builder.build())
}