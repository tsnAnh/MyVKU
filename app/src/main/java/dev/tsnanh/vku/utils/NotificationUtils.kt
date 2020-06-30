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
import org.koin.java.KoinJavaComponent

private const val NOTIFICATION_ID = 0

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

fun NotificationManager.sendNotification(
    title: String,
    message: String,
    threadId: String,
    threadTitle: String,
    applicationContext: Context
) {

    val pendingIntent = NavDeepLinkBuilder(applicationContext)
        .setArguments(
            bundleOf(
                Constants.THREAD_ID_KEY to threadId,
                Constants.THREAD_TITLE_KEY to threadTitle
            )
        )
        .setDestination(R.id.navigation_replies)
        .setGraph(R.navigation.nav_graph)
        .createPendingIntent()

    val builder =
        NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.new_thread_channel_id)
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.sendNotificationWithProgress(
    title: String,
    message: String,
    applicationContext: Context
) {
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.new_thread_channel_id)
    )
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(message)
        .setProgress(0, 0, true)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.sendSchoolReminderNotification(
    uid: Int,
    title: String,
    message: String,
    applicationContext: Context
) {
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.school_reminder_channel_id)
    )
        .setContentTitle(title)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(message)
        .setAutoCancel(true)

    notify(uid, builder.build())
}

fun NotificationManager.sendCloudMessageNotification(
    applicationContext: Context,
    payload: Map<String, String>
) {
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.firebase_forum_notification_channel)
    ).apply {
        setContentTitle("casdasdasd")
        setContentText("cccsadasd")
        setAutoCancel(true)
        setSmallIcon(R.mipmap.ic_launcher)
    }

    notify(2, builder.build())
}