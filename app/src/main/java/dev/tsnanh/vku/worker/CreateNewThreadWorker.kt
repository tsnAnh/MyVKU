package dev.tsnanh.vku.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.network.NetworkCreateThreadContainer
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateNewThreadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        Timber.d(inputData.toString())

        val token = inputData.getStringArray("id_token")?.get(0)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter =
            moshi.adapter(NetworkCreateThreadContainer::class.java)

        val jsonContainer = inputData.getStringArray("container")?.get(0)
        val container = withContext(Dispatchers.IO) {
            jsonAdapter.fromJson(jsonContainer!!)
        }

        val listImageURL = inputData.getStringArray("imageURL")
        if (listImageURL != null && listImageURL.isNotEmpty()) {
            container?.post?.images = listImageURL.toList()
        }
        Timber.d(container.toString())

        val deferredThread = async {
            VKUServiceApi.network.createThread("Bearer $token", container!!).asDomainModel()
        }
        val threadJsonAdapter = moshi.adapter(ForumThread::class.java)
        val threadJson = threadJsonAdapter.toJson(deferredThread.await())

        Timber.d("CCC $threadJson")
        Result.success(workDataOf("thread" to threadJson))
    }
}