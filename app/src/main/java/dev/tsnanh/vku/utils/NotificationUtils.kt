/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import dev.tsnanh.vku.R

private const val NOTIFICATION_ID = 0

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

    notify(NOTIFICATION_ID, builder.build())
}