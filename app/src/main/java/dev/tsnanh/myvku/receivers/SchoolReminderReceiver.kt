package dev.tsnanh.myvku.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dev.tsnanh.myvku.services.SchoolReminderService
import timber.log.Timber

class SchoolReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val newIntent = Intent(context, SchoolReminderService::class.java).apply {
            putExtra("email", intent.getStringExtra("email"))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(newIntent)
        } else {
            context.startService(newIntent)
        }
        Timber.i("Receiver called")
    }
}
