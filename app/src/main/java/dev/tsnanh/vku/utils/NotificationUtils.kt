/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dev.tsnanh.vku.R

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
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