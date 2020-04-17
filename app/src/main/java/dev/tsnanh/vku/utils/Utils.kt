/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun convertTimestampToDateString(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy/MM/dd - HH:mm:ss", Locale.getDefault())

    return formatter.format(date)
}

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