package dev.tsnanh.myvku.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dev.tsnanh.myvku.utils.setSchoolReminderAlarm
import javax.inject.Inject

class RestartSchoolReminderNotificationReceiver @Inject constructor() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                GoogleSignIn.getLastSignedInAccount(context)?.email?.let {
                    context.setSchoolReminderAlarm(it)
                }
            }
        }
    }
}