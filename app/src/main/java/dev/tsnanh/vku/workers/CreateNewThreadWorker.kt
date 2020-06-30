/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.squareup.moshi.Moshi
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.usecases.CreateNewThreadUseCase
import dev.tsnanh.vku.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class CreateNewThreadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        //Use Case
        val createNewThreadUseCase by inject(CreateNewThreadUseCase::class.java)
        // Get data from inputData
        val token = inputData.getString(Constants.TOKEN_KEY)!!
        val threadJsonString = inputData.getString(Constants.THREAD_WORK_KEY)!!
        // Logging
        Timber.d(threadJsonString)
        Timber.d(token)

        // Moshi instance
        val moshi by inject(Moshi::class.java)

        // JsonAdapter
        val threadJsonAdapter = moshi.adapter(ForumThread::class.java)

        // Thread
        val thread =
            withContext(Dispatchers.IO) { threadJsonAdapter.fromJson(threadJsonString) }

        val deferredThread = async {
            createNewThreadUseCase.execute(token, thread!!, thread.forumId)
        }

        // Created Thread response
        val responseThreadId = deferredThread.await().data?.id

        Result.success(workDataOf("threadId" to responseThreadId))
    }
}