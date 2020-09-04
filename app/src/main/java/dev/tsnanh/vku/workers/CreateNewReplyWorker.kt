/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.workers

import android.content.Context
import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ReplyRepoImpl
import dev.tsnanh.vku.domain.usecases.CreateNewReplyUseCase
import dev.tsnanh.vku.domain.usecases.CreateNewReplyUseCaseImpl
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.getFilePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CreateNewReplyWorker @WorkerInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork() = coroutineScope {
        val createNewReplyUseCase: CreateNewReplyUseCase = CreateNewReplyUseCaseImpl(ReplyRepoImpl())
        val moshi = Moshi.Builder().build()
        // Get data from inputData
        val token = inputData.getString(Constants.TOKEN_KEY)!!
        val threadId = inputData.getString("threadId")!!
        val replyJsonString = inputData.getString(Constants.REPLY_KEY)!!
        val urisJsonString = inputData.getString(Constants.IMAGES_KEY)!!
        val quotedReplyId = inputData.getString(Constants.QUOTED_REPLY)

        val jsonAdapter =
            moshi.adapter(NetworkReply::class.java)
        val type =
            Types.newParameterizedType(List::class.java, String::class.java)
        val uriAdapter = moshi.adapter<List<String>>(type)

        val reply = withContext(Dispatchers.IO) {
            jsonAdapter.fromJson(replyJsonString)
        }

        val uris = withContext(Dispatchers.IO) { uriAdapter.fromJson(urisJsonString) }
        val parts = uris?.map { uriString ->
            val imageUri = Uri.parse(uriString)
            val descriptor =
                context.contentResolver.openFileDescriptor(imageUri, "r", null)
            val inputStream = FileInputStream(descriptor!!.fileDescriptor)

            val fileImage = File(
                context.cacheDir,
                context.contentResolver.getFilePath(imageUri)
            )

            val outputStream = FileOutputStream(fileImage)
            inputStream.copyTo(outputStream)

            val requestBody = RequestBody.create(
                MediaType.parse("image/${fileImage.extension}"),
                fileImage
            )
            MultipartBody.Part.createFormData("newImage", fileImage.name, requestBody)
        }?.toTypedArray()

        val deferred = async {
            parts?.let {
                with(createNewReplyUseCase) {
                    try {
                        execute(
                            token,
                            threadId,
                            RequestBody.create(MediaType.parse("text/plain"), reply!!.content),
                            parts,
                            if (quotedReplyId != null) {
                                RequestBody.create(MediaType.parse("text/plain"), quotedReplyId)
                            } else {
                                null
                            }
                        )
                    } catch (e: Exception) {
                        // TODO: 18/07/2020 delete threads when create reply fail
                        Resource.Error<News>("failToCreateReply").also {
                            Result.failure()
                        }
                    }
                }
            }
        }

        deferred.await().also {
            Timber.d("${it?.data}")
        }

        Result.success(workDataOf("threadId" to threadId))
    }
}