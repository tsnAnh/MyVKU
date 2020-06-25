/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import android.app.NotificationManager
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.squareup.moshi.Moshi
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.entities.CreateThreadContainer
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.sendNotificationWithProgress
import dev.tsnanh.vku.workers.CreateNewThreadWorker
import dev.tsnanh.vku.workers.UploadPostImageWorker
import org.koin.java.KoinJavaComponent.inject

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    private val account = GoogleSignIn.getLastSignedInAccount(getApplication())
    private val notificationManager by inject(NotificationManager::class.java)

    // TODO: Create Notifications LiveData

    fun createNewThread(list: List<Uri>, thread: ForumThread, post: Reply) {
        account?.idToken?.let {
            it.let { token ->
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
                    Constants.CONTAINER_KEY to json,
                    Constants.TOKEN_KEY to token
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
                        getApplication<Application>().getString(R.string.text_uploading),
                        "",
                        getApplication()
                    )
                    val requests = list.map { uri ->
                        OneTimeWorkRequestBuilder<UploadPostImageWorker>()
                            .setConstraints(constraint)
                            .setInputData(
                                workDataOf(
                                    Constants.IMAGE_KEY to uri.toString(),
                                    Constants.TOKEN_KEY to token,
                                    Constants.UNIQUE_ID_KEY to account.id
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