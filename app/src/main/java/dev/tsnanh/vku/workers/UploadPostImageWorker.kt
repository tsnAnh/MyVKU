/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.workers

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dev.tsnanh.vku.domain.network.VKUServiceApi
import dev.tsnanh.vku.utils.getFilePath
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

internal const val IMAGE_URL = "imageURL"

class UploadPostImageWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        setProgress(workDataOf(WorkUtil.Progress to 0))
        val token = inputData.getString("id_token")
        val uid = inputData.getString("uid")!!
        val imageUri = Uri.parse(inputData.getString("image"))

        setProgress(workDataOf(WorkUtil.Progress to 20))

        val descriptor =
            context.contentResolver.openFileDescriptor(imageUri, "r", null)
        val inputStream = FileInputStream(descriptor!!.fileDescriptor)

        val fileImage = File(
            context.cacheDir,
            "${UUID.randomUUID().toString()
                .substring(0, 8)} - ${context.contentResolver.getFilePath(imageUri)}"
        )

        setProgress(workDataOf(WorkUtil.Progress to 40))

        val outputStream = FileOutputStream(fileImage)
        inputStream.copyTo(outputStream)

        setProgress(workDataOf(WorkUtil.Progress to 60))

        val requestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            fileImage
        )
        val filePart =
            MultipartBody.Part.createFormData("image", fileImage.name, requestBody)
        // Upload image
        setProgress(workDataOf(WorkUtil.Progress to 80))
        val imageURL = async {
            VKUServiceApi.network.uploadImage("Bearer $token", uid, filePart)
        }

        setProgress(workDataOf(WorkUtil.Progress to 100))

        Result.success(workDataOf(IMAGE_URL to imageURL.await()))
    }
}