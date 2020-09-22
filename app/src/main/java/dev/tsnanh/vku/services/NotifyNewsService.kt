package dev.tsnanh.vku.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NotifyNewsService @Inject constructor(
    private val retrieveNewsUseCase: RetrieveNewsUseCase,
    private val dao: VKUDao,
) : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch {

        }
        Timber.i("Refreshed News!")
        return START_STICKY
    }

    private var unique = 0
}

const val NEWS_NOTIFY_CHANNEL_ID = "dev.tsnanh.myvku.channel.newsnotify"