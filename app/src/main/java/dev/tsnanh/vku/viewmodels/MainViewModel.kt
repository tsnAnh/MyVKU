/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.domain.usecases.LoginUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCase
import dev.tsnanh.vku.utils.ConnectivityLiveData
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.toListStringUri
import dev.tsnanh.vku.workers.CreateNewReplyWorker
import dev.tsnanh.vku.workers.CreateNewThreadWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val retrieveNewsUseCase: RetrieveNewsUseCase,
    private val retrieveUserTimetableLiveDataUseCase: RetrieveUserTimetableLiveDataUseCase,
    private val workManager: WorkManager,
    moshi: Moshi,
    private val mGoogleSignInClient: GoogleSignInClient,
    private val loginUseCase: LoginUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var isLoggedIn = false

    @RequiresApi(Build.VERSION_CODES.N)
    val connectivityLiveData = ConnectivityLiveData(context.getSystemService())

    private var _loginState = MutableLiveData(LoginState.UNAUTHENTICATED)
    val loginState: LiveData<LoginState>
        get() = _loginState

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    // Create JsonAdapter
    private val threadJsonAdapter =
        moshi.adapter(ForumThread::class.java)
    private val replyJsonAdapter =
        moshi.adapter(NetworkReply::class.java)
    private val type =
        Types.newParameterizedType(List::class.java, String::class.java)
    private val uriAdapter = moshi.adapter<List<String>>(type)

    // Work Constraint
    private val constraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    // TODO: Create Notifications LiveData

    fun createNewThread(list: List<Uri>, thread: ForumThread, reply: NetworkReply) {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener {
            if (it.isComplete) {
                try {
                    // Get token and uid from Google Sign In
                    val token = it.getResult(ApiException::class.java)?.idToken!!

                    // Prepare WorkData
                    val threadData = workDataOf(
                        Constants.THREAD_WORK_KEY to threadJsonAdapter.toJson(thread),
                        Constants.TOKEN_KEY to token
                    )
                    val replyData = workDataOf(
                        Constants.REPLY_KEY to replyJsonAdapter.toJson(reply),
                        Constants.TOKEN_KEY to token,
                        Constants.IMAGES_KEY to uriAdapter.toJson(list.toListStringUri()),
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
                    workManager
                        .beginWith(createThreadRequest)
                        .then(createReplyRequest)
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
        reply: NetworkReply,
        images: List<Uri>,
        quotedReplyId: String?,
    ) {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener {
            if (it.isComplete) {
                try {
                    val token = it.getResult(ApiException::class.java)?.idToken

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

    fun silentSignIn() {
        if (isLoggedIn) return
        viewModelScope.launch {
            val deferredIdToken = mGoogleSignInClient.silentSignIn().asDeferred()
            val deferredInstanceId = FirebaseInstanceId.getInstance().instanceId.asDeferred()

            val idToken = deferredIdToken.await().idToken
            val tokenFCM = deferredInstanceId.await().token

            if (idToken != null) {
                loginUseCase.execute(idToken, LoginBody(tokenFCM = tokenFCM))
                    .flowOn(Dispatchers.IO)
                    .onStart { _loginState.postValue(LoginState.AUTHENTICATING) }
                    .catch { t ->
                        _error.postValue(t)
                        _loginState.postValue(LoginState.AUTHENTICATED)
                    }
                    .collect {
                        _loginState.postValue(LoginState.AUTHENTICATED)
                        isLoggedIn = true
                    }
            }
        }
    }

    fun refreshData(email: String) {
        viewModelScope.launch {
            retrieveNewsUseCase.refresh()
            retrieveUserTimetableLiveDataUseCase.refresh(email)
        }
    }
}