package dev.tsnanh.myvku.services

import android.app.NotificationManager
import android.content.Intent
import android.os.IBinder
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.myvku.utils.sendLatestNewsNotification
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotifyNewsService @Inject constructor(
    private val retrieveNewsUseCase: RetrieveNewsUseCase,
    private val dao: VKUDao,
) : LifecycleService() {
    private val notificationManager by lazy {
        getSystemService<NotificationManager>()
    }
    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        lifecycleScope.launch {
            val localNews = dao.getAllNewsFlow().firstOrNull() ?: emptyList()
            val news = VKUServiceApi.network.getNews()
            val latestNews = news - localNews
            if (latestNews.isNotEmpty()) {
                retrieveNewsUseCase.refresh()
                notificationManager?.sendLatestNewsNotification(this@NotifyNewsService, latestNews)
            }
        }
        return START_STICKY
    }
}

const val NEWS_NOTIFY_CHANNEL_ID = "dev.tsnanh.myvku.channel.NOTIFY_NEWS"
