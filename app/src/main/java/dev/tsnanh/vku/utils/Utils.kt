/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

@file:Suppress("DEPRECATION")

package dev.tsnanh.vku.utils

import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dev.tsnanh.vku.domain.entities.Subject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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
    action: ((View) -> Unit)? = null,
    anchorView: View? = null
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
    anchorView?.let {
        bar.setAnchorView(it)
    }
    bar.show()
}

fun List<Uri>.toListStringUri(): List<String> {
    return map {
        it.toString()
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}


fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }

    return result
}
