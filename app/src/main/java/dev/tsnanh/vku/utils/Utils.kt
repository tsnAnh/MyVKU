package dev.tsnanh.vku.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern


fun convertJsTimeToJavaString(jsTime: String) =
    jsTime.substring(0, 19).replace("T", " ")

fun getRealPathFromDocumentUri(
    context: Context,
    uri: Uri
): String {
    Timber.d(uri.path)
    var filePath = ""
    val p: Pattern = Pattern.compile("(\\d+)$")
    val m: Matcher = p.matcher(uri.toString())
    if (!m.find()) {
        return filePath
    }
    val imgId: String = m.group()
    val column = arrayOf("_data")
    val sel = MediaStore.Images.Media._ID + "=?"
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null, sel, arrayOf(imgId), null
    )

    val columnIndex = cursor!!.getColumnIndex(column[0])
    if (cursor.moveToFirst()) {
        filePath = cursor.getString(columnIndex)
        Timber.d("filePath: $filePath")
    }
    cursor.close()
    return filePath
}