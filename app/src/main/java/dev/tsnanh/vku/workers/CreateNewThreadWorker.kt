/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.entities.CreateThreadContainer
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.network.VKUServiceApi
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.Constants.Companion.IMAGE_URL_KEY
import dev.tsnanh.vku.utils.Constants.Companion.THREAD_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateNewThreadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        setProgress(workDataOf(WorkUtil.Progress to 0))

        val token = inputData.getStringArray(Constants.TOKEN_KEY)?.get(0)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter =
            moshi.adapter(CreateThreadContainer::class.java)

        setProgress(workDataOf(WorkUtil.Progress to 20))

        val jsonContainer = inputData.getStringArray(Constants.CONTAINER_KEY)?.get(0)
        val container = withContext(Dispatchers.IO) {
            jsonAdapter.fromJson(jsonContainer!!)
        }

        setProgress(workDataOf(WorkUtil.Progress to 40))

        val listImageURL = inputData.getStringArray(IMAGE_URL_KEY)
        if (listImageURL != null && listImageURL.isNotEmpty()) {
            container?.reply?.images = listImageURL.toList()
        }
        setProgress(workDataOf(WorkUtil.Progress to 60))

        Timber.d("$token")
        val deferredThread = async {
            VKUServiceApi.network.createThread("$token", container!!, "")
        }
        setProgress(workDataOf(WorkUtil.Progress to 80))
        val threadJsonAdapter = moshi.adapter(ForumThread::class.java)
        val threadJson = threadJsonAdapter.toJson(deferredThread.await())

        setProgress(workDataOf(WorkUtil.Progress to 100))
        Result.success(workDataOf(THREAD_KEY to threadJson))
    }
}