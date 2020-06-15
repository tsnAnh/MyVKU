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
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.network.VKUServiceApi
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.Constants.Companion.IMAGE_URL_KEY
import dev.tsnanh.vku.utils.Constants.Companion.POST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class CreateNewPostWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork() = coroutineScope {
        setProgress(workDataOf(WorkUtil.Progress to 0))
        val idToken = inputData.getStringArray(Constants.TOKEN_KEY)!![0]
        val jsonAdapter =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Reply::class.java)
        val postJson = inputData.getStringArray(Constants.REPLY_KEY)!![0]

        setProgress(workDataOf(WorkUtil.Progress to 33))

        val post = withContext(Dispatchers.IO) {
            jsonAdapter.fromJson(postJson)
        }
        val listImageURL = inputData.getStringArray(IMAGE_URL_KEY)
        if (listImageURL != null && listImageURL.isNotEmpty()) {
            post?.images = listImageURL.toList()
        }

        setProgress(workDataOf(WorkUtil.Progress to 66))

        val deferred = async {
            VKUServiceApi.network.newReply(idToken, post!!)
        }

        setProgress(workDataOf(WorkUtil.Progress to 100))

        Result.success(workDataOf(POST to jsonAdapter.toJson(deferred.await())))
    }
}