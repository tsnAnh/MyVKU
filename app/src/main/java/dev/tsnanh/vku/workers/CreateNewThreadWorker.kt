/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.workers

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

const val THREAD = "thread"

class CreateNewThreadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        setProgress(workDataOf(WorkUtil.Progress to 0))

        val token = inputData.getStringArray("id_token")?.get(0)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter =
            moshi.adapter(NetworkCreateThreadContainer::class.java)

        setProgress(workDataOf(WorkUtil.Progress to 20))

        val jsonContainer = inputData.getStringArray("container")?.get(0)
        val container = withContext(Dispatchers.IO) {
            jsonAdapter.fromJson(jsonContainer!!)
        }

        setProgress(workDataOf(WorkUtil.Progress to 40))

        val listImageURL = inputData.getStringArray(IMAGE_URL)
        if (listImageURL != null && listImageURL.isNotEmpty()) {
            container?.post?.images = listImageURL.toList()
        }
        Timber.d(container.toString())
        setProgress(workDataOf(WorkUtil.Progress to 60))

        val deferredThread = async {
            VKUServiceApi.network.createThread("Bearer $token", container!!).asDomainModel()
        }
        setProgress(workDataOf(WorkUtil.Progress to 80))
        val threadJsonAdapter = moshi.adapter(ForumThread::class.java)
        val threadJson = threadJsonAdapter.toJson(deferredThread.await())

        Timber.d("CCC $threadJson")
        setProgress(workDataOf(WorkUtil.Progress to 100))
        Result.success(workDataOf(THREAD to threadJson))
    }
}