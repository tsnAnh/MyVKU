/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.toListStringUri
import dev.tsnanh.vku.workers.CreateNewReplyWorker
import dev.tsnanh.vku.workers.CreateNewThreadWorker
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class MainViewModel : ViewModel() {
    private val workManager by inject(WorkManager::class.java)
    private val mGoogleSignInClient by inject(GoogleSignInClient::class.java)
//    private val notificationManager by inject(NotificationManager::class.java)

    // Get Moshi instance from DI
    private val moshi by inject(Moshi::class.java)

    // Create JsonAdapter
    private val threadJsonAdapter =
        moshi.adapter(ForumThread::class.java)
    private val replyJsonAdapter =
        moshi.adapter(Reply::class.java)
    private val type =
        Types.newParameterizedType(List::class.java, String::class.java)
    private val uriAdapter = moshi.adapter<List<String>>(type)

    // Work Constraint
    private val constraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    // TODO: Create Notifications LiveData

    fun createNewThread(list: List<Uri>, thread: ForumThread, reply: Reply) {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener {
            if (it.isComplete) {
                try {
                    // Get token and uid from Google Sign In
                    val token = it.getResult(ApiException::class.java)!!.idToken!!

                    // Prepare WorkData
                    val threadData = workDataOf(
                        Constants.THREAD_WORK_KEY to threadJsonAdapter.toJson(thread),
                        Constants.TOKEN_KEY to token
                    )
                    val replyData = workDataOf(
                        Constants.REPLY_KEY to replyJsonAdapter.toJson(reply),
                        Constants.TOKEN_KEY to token,
                        Constants.IMAGES_KEY to uriAdapter.toJson(list.toListStringUri())
                    )

                    // Create WorkRequest
                    val createThreadRequest =
                        OneTimeWorkRequestBuilder<CreateNewThreadWorker>()
                            .setConstraints(constraint)
                            .setInputData(threadData)
                            .build()
                    val createReplyRequest =
                        OneTimeWorkRequestBuilder<CreateNewReplyWorker>()
                            .setConstraints(constraint)
                            .setInputData(replyData)
                            .addTag("dev.tsnanh.newreply")
                            .build()

                    // Enqueue Work Process
                    workManager.beginWith(createThreadRequest).then(createReplyRequest)
                        .enqueue()
                } catch (e: Exception) {
                    Timber.e(e)
                    // TODO: Handle token exception
                }
            }
        }
    }

    fun createNewReply(
        threadId: String,
        reply: Reply,
        images: List<Uri>,
        quotedReplyId: String?
    ) {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener {
            if (it.isComplete) {
                try {
                    val token = it.getResult(ApiException::class.java)!!.idToken

                    val replyData = workDataOf(
                        Constants.REPLY_KEY to replyJsonAdapter.toJson(reply),
                        Constants.TOKEN_KEY to token,
                        Constants.IMAGES_KEY to uriAdapter.toJson(images.toListStringUri()),
                        Constants.QUOTED_REPLY to quotedReplyId,
                        "threadId" to threadId
                    )

                    val createReplyRequest =
                        OneTimeWorkRequestBuilder<CreateNewReplyWorker>()
                            .setConstraints(constraint)
                            .setInputData(replyData)
                            .addTag("dev.tsnanh.newreply")
                            .build()

                    workManager.enqueue(createReplyRequest)
                } catch (e: Exception) {
                    Timber.e(e)
                    // TODO: Handle exception
                }
            }
        }
    }
}