/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import dev.tsnanh.vku.domain.entities.Subject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Convert Javascript timestamp to Java DateTime
 * @return datetime String
 */
fun Long.convertTimestampToDateString(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.getDefault())

    return formatter.format(date)
}

/**
 * Get file path from ContentResolver
 * @param uri Uri
 * @return fileName String
 */
fun ContentResolver.getFilePath(uri: Uri): String {
    var name = ""

    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    Timber.d(name)
    return name
}

fun Subject.checkSubjectHasValidAlarm() = try {
    lesson.getHourFromStringLesson()
    lesson.getMinutesFromStringLesson()
    dayOfWeek.getDayOfWeekFromString()
    true
} catch (e: IllegalArgumentException) {
    false
}