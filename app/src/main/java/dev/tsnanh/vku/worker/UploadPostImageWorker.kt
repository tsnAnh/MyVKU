package dev.tsnanh.vku.worker

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.utils.getFilePath
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UploadPostImageWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        val token = inputData.getString("id_token")
        val uid = inputData.getString("uid")!!
        val imageUri = Uri.parse(inputData.getString("image"))

        val descriptor = context.contentResolver.openFileDescriptor(imageUri, "r", null)
        val inputStream = FileInputStream(descriptor!!.fileDescriptor)

        val fileImage = File(context.cacheDir, context.contentResolver.getFilePath(imageUri))

        val outputStream = FileOutputStream(fileImage)
        inputStream.copyTo(outputStream)

        val requestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            fileImage
        )
        val filePart = MultipartBody.Part.createFormData("image", fileImage.name, requestBody)
        // Upload image
        val imageURL = async {
            VKUServiceApi.network.uploadImage("Bearer $token", uid, filePart)
        }

        Result.success(workDataOf("imageURL" to imageURL.await()))
    }
}