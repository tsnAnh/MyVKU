/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.worker

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

class CreateNewPostWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork() = coroutineScope {
        val idToken = inputData.getStringArray("id_token")!![0]
        val jsonAdapter =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(NetworkPost::class.java)
        val postJson = inputData.getStringArray("post")!![0]

        val post = withContext(Dispatchers.IO) {
            jsonAdapter.fromJson(postJson)
        }
        val listImageURL = inputData.getStringArray("imageURL")
        if (listImageURL != null && listImageURL.isNotEmpty()) {
            post?.images = listImageURL.toList()
        }

        val deferred = async {
            VKUServiceApi.network.newReply("Bearer $idToken", post!!)
        }

        Result.success(workDataOf("reply" to jsonAdapter.toJson(deferred.await())))
    }
}