/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import dev.tsnanh.vku.domain.entities.Subject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Convert Javascript timestamp to Java DateTime
 * @return datetime String
 */
fun Long.convertToDateString(format: String = Constants.DATE_FORMAT_PATTERN_DASH): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(format, Locale.getDefault())

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

fun String.checkValidWeek(): Boolean = length >= 3

fun showSnackbarWithAction(
    view: View,
    msg: String,
    actionButton: String? = null,
    action: ((View) -> Unit)? = null
) {
    val bar = Snackbar
        .make(
            view, msg, if (!actionButton.isNullOrBlank()) {
                Snackbar.LENGTH_INDEFINITE
            } else {
                Snackbar.LENGTH_LONG
            }
        )

    bar.setAction(actionButton, if (action != null) {
        action
    } else {
        { bar.dismiss() }
    })
    bar.show()
}

fun List<Uri>.toListStringUri(): List<String> {
    return map {
        it.toString()
    }
}