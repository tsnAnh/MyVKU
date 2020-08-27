/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.squareup.moshi.Moshi
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.repositories.ThreadRepoImpl
import dev.tsnanh.vku.domain.usecases.CreateNewThreadUseCase
import dev.tsnanh.vku.domain.usecases.CreateNewThreadUseCaseImpl
import dev.tsnanh.vku.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber

class CreateNewThreadWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        val createNewThreadUseCase: CreateNewThreadUseCase = CreateNewThreadUseCaseImpl(
            ThreadRepoImpl())
        val moshi = Moshi.Builder().build()
        // Get data from inputData
        val token = inputData.getString(Constants.TOKEN_KEY)!!
        val threadJsonString = inputData.getString(Constants.THREAD_WORK_KEY)!!
        // Logging
        Timber.d(threadJsonString)
        Timber.d(token)

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