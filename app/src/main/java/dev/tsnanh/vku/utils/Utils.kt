/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

@file:Suppress("DEPRECATION")

package dev.tsnanh.vku.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.provider.OpenableColumns
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import androidx.annotation.WorkerThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.entities.Subject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Convert Unix timestamp to Java Date
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

fun Subject.hasValidAlarm() = try {
    lesson.getHourFromLesson()
    lesson.getMinutesFromStringLesson()
    dayOfWeek.getDayOfWeekFromString()
    true
} catch (e: IllegalArgumentException) {
    false
}

val String.isValidWeek: Boolean
    get() = length >= 3

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


fun Context.isInternetAvailableApi23(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

@WorkerThread
fun blurBitmap(bitmap: Bitmap, applicationContext: Context): Bitmap {
    lateinit var rsContext: RenderScript
    try {

        // Create the output bitmap
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap.height, bitmap.config
        )

        // Blur the image
        rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
        val inAlloc = Allocation.createFromBitmap(rsContext, bitmap)
        val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)
        val theIntrinsic = ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext))
        theIntrinsic.apply {
            setRadius(10f)
            theIntrinsic.setInput(inAlloc)
            theIntrinsic.forEach(outAlloc)
        }
        outAlloc.copyTo(output)

        return output
    } finally {
        rsContext.finish()
    }
}

fun String.unescapeJava(): String {
    var escaped1 = this
    if (escaped1.indexOf("\\u") == -1) return escaped1
    var processed = ""
    var position = escaped1.indexOf("\\u")
    while (position != -1) {
        if (position != 0) processed += escaped1.substring(0, position)
        val token = escaped1.substring(position + 2, position + 6)
        escaped1 = escaped1.substring(position + 6)
        processed += token.toInt(16).toChar()
        position = escaped1.indexOf("\\u")
    }
    processed += escaped1
    return processed
}

fun String.getTypeDrawable(): Int {
    return when (this) {
        "xls", "xlsx" -> R.drawable.sheets
        "doc", "docx" -> R.drawable.doc
        "png", "jpg", "jpeg", "webp" -> R.drawable.image
        "pdf" -> R.drawable.pdf
        else -> R.drawable.file
    }
}