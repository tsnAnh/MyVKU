package dev.tsnanh.myvku.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.tsnanh.myvku.services.NotifyNewsService

class NotifyNewsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        with(Intent(context, NotifyNewsService::class.java)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context?.startForegroundService(this)
            } else {
                context?.startService(this)
            }
        }
    }
}