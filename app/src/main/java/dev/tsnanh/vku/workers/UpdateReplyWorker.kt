package dev.tsnanh.vku.workers

import android.content.Context
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dev.tsnanh.vku.domain.entities.WorkResult
import dev.tsnanh.vku.domain.usecases.UpdateReplyUseCase
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.getFilePath
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UpdateReplyWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = coroutineScope {
        val useCase by inject(UpdateReplyUseCase::class.java)
        // Input Data
        val idToken = inputData.getString(Constants.TOKEN_KEY)!!
        val replyId = inputData.getString("replyId")!!
        val newImageUrisString = inputData.getStringArray("newImage")
        val content = inputData.getString("content")!!
        val imagesString = inputData.getStringArray("images")
        Timber.i("IdToken: $idToken")
        Timber.i("replyId: $replyId")
        imagesString?.forEach {
            Timber.i(it)
        }
        Timber.i("content: $content")
        Timber.i("images: $imagesString")

        // Convert to List<Uri>
        val newImage = newImageUrisString
            ?.map {
                it.toUri()
            }?.map { uri ->
                val descriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
                val inputStream = FileInputStream(descriptor?.fileDescriptor)

                val file = File(
                    context.cacheDir,
                    context.contentResolver.getFilePath(uri)
                )

                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                val requestBody = RequestBody.create(
                    MediaType.parse("image/${file.extension}"),
                    file
                )
                MultipartBody.Part.createFormData("newImage", file.name, requestBody)
            }?.toTypedArray()
        val images = imagesString?.map {
            RequestBody.create(
                MediaType.parse("text/plain"),
                it.replace("\\", "\\\\")
            )
        }?.toTypedArray()
        Timber.d(images.toString())

        val contentBody = RequestBody.create(
            MediaType.parse("text/plain"),
            content
        )

        val deferred = async {
            useCase.invoke(idToken, replyId, newImage, contentBody, images)
        }
        when (deferred.await()) {
            is WorkResult.Success -> {
                Result.success(workDataOf("threadId" to deferred.await().data?.threadId))
            }
            is WorkResult.Error -> {
                Timber.e(deferred.await().message)
                Result.failure()
            }
        }
    }

}