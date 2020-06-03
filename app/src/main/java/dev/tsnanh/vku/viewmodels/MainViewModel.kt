/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.app.Application
import android.app.NotificationManager
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import dev.tsnanh.vku.domain.entities.CreateThreadContainer
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.utils.sendNotificationWithProgress
import dev.tsnanh.vku.workers.CreateNewThreadWorker
import dev.tsnanh.vku.workers.UploadPostImageWorker
import org.koin.java.KoinJavaComponent.inject

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    private val notificationManager by inject(NotificationManager::class.java)

    /* unused
    fun create(thread: ForumThread, post: Post) {
        val container = NetworkCreateThreadContainer(thread.asNetworkModel(), post.asNetworkModel())
        val task = firebaseUser?.getIdToken(true)
        task?.addOnSuccessListener {
            viewModelScope.launch {
                val responseThread = withContext(Dispatchers.IO) {
                    it.token?.let { it1 ->
                        VKUServiceApi.network.createThread("Bearer $it1", container)
                    }
                }
            }
        }
    }
    */

    fun createNewThread(
        list: List<Uri>,
        thread: ForumThread,
        post: Reply
    ) {
        firebaseUser?.getIdToken(true)?.addOnSuccessListener {
            it?.token?.let { token ->
                val moshi = Moshi.Builder().build()
                val jsonAdapter =
                    moshi.adapter(CreateThreadContainer::class.java)

                val container =
                    CreateThreadContainer(thread, post)
                val json = jsonAdapter.toJson(container)
                val constraint = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val threadData = workDataOf(
                    "container" to json,
                    "id_token" to token
                )
                val createThreadRequest =
                    OneTimeWorkRequestBuilder<CreateNewThreadWorker>()
                        .setConstraints(constraint)
                        .setInputMerger(ArrayCreatingInputMerger::class)
                        .setInputData(threadData)
                        .addTag(TAG_NEW_THREAD)
                        .build()

                if (list.isEmpty()) {
                    workManager.enqueue(
                        createThreadRequest
                    )
                } else {
                    notificationManager.sendNotificationWithProgress(
                        "Uploading",
                        "",
                        getApplication()
                    )
                    val requests = list.map { uri ->
                        OneTimeWorkRequestBuilder<UploadPostImageWorker>()
                            .setConstraints(constraint)
                            .setInputData(
                                workDataOf(
                                    "image" to uri.toString(),
                                    "id_token" to token,
                                    "uid" to firebaseUser.uid
                                )
                            )
                            .build()
                    }

                    workManager.beginWith(requests).then(createThreadRequest).enqueue()
                }
            }
        }
    }
}