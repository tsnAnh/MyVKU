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
import dev.tsnanh.vku.network.NetworkPost
import dev.tsnanh.vku.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

const val POST = "post"

class CreateNewPostWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork() = coroutineScope {
        setProgress(workDataOf(WorkUtil.Progress to 0))
        val idToken = inputData.getStringArray("id_token")!![0]
        val jsonAdapter =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(NetworkPost::class.java)
        val postJson = inputData.getStringArray("post")!![0]

        setProgress(workDataOf(WorkUtil.Progress to 33))

        val post = withContext(Dispatchers.IO) {
            jsonAdapter.fromJson(postJson)
        }
        val listImageURL = inputData.getStringArray(IMAGE_URL)
        if (listImageURL != null && listImageURL.isNotEmpty()) {
            post?.images = listImageURL.toList()
        }

        setProgress(workDataOf(WorkUtil.Progress to 66))

        val deferred = async {
            VKUServiceApi.network.newReply("Bearer $idToken", post!!)
        }

        setProgress(workDataOf(WorkUtil.Progress to 100))

        Result.success(workDataOf(POST to jsonAdapter.toJson(deferred.await())))
    }
}