package dev.tsnanh.myvku.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCase
import kotlinx.coroutines.GlobalScope
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
