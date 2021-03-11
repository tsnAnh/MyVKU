package dev.tsnanh.myvku.receivers

import android.app.DownloadManager
import android.content.*
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class AttachmentReceiver @Inject constructor() : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == p1?.action) {
            val downloadId = p1.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)

            val dm = p0?.getSystemService<DownloadManager>()
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val cursor = dm?.query(query)
            if (cursor?.moveToFirst()!!) {
                val downloadStatus =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val localUri =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val downloadMimeType =
                    cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE))
                cursor.close()
                var newUri: Uri? = localUri.toUri()
                if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL && localUri != null) {
                    localUri.toUri().let { uri ->
                        if (ContentResolver.SCHEME_FILE == uri.scheme) {
                            val file = uri.path?.let { File(it) }
                            newUri = file?.let {
                                FileProvider.getUriForFile(
                                    p0, "dev.tsnanh.myvku.fileprovider",
                                    it
                                )
                            }
                        }
                    }
                }
                val openIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(newUri, downloadMimeType)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                try {
                    p0.startActivity(openIntent)
                } catch (e: ActivityNotFoundException) {
                    Timber.e(e)
                }
            }
        }
        p0?.unregisterReceiver(this)
    }
}
