/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.usecases.CreateNewReplyUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCase
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.Constants.Companion.POST_TAG
import dev.tsnanh.vku.workers.CreateNewPostWorker
import dev.tsnanh.vku.workers.UploadPostImageWorker
import org.koin.java.KoinJavaComponent.inject

class NewReplyViewModel(quotedPostId: String, application: Application) :
    AndroidViewModel(application) {

    private val createNewReplyUseCase by inject(CreateNewReplyUseCase::class.java)
    private val retrieveReplyByIdUseCase by inject(RetrieveReplyByIdUseCase::class.java)
//    val quotedPost = if (quotedPostId.isNotEmpty()) {
//        retrieveReplyByIdUseCase.execute(quotedPostId)
//    } else {
//        MutableLiveData(Resource.Error<Reply>("empty"))
//    }

    private val _pickerHasImage = MutableLiveData(false)
    val pickerHasImage: LiveData<Boolean>
        get() = _pickerHasImage

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun onNavigatedBack() {
        _navigateBack.value = null
    }

    fun onPickerHasImage() {
        _pickerHasImage.value = true
    }

    fun onPickerHasNoImage() {
        _pickerHasImage.value = false
    }

    fun newReply(post: Reply, images: List<Uri>) {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val networkPost = post
        val adapter =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Reply::class.java)
        val json = adapter.toJson(networkPost)
        val account = GoogleSignIn.getLastSignedInAccount(getApplication())
        account?.idToken?.let {
            it.let { token ->
                val createNewReply = OneTimeWorkRequestBuilder<CreateNewPostWorker>()
                    .setConstraints(constraint)
                    .setInputMerger(ArrayCreatingInputMerger::class.java)
                    .addTag(POST_TAG)
                    .setInputData(
                        workDataOf(
                            Constants.TOKEN_KEY to token,
                            Constants.REPLY_KEY to json
                        )
                    )
                    .build()
                if (images.isEmpty()) {
                    WorkManager.getInstance(getApplication()).enqueue(createNewReply)
                } else {
                    val requests = images.map { uri ->
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
                    WorkManager.getInstance(getApplication())
                        .beginWith(requests)
                        .then(createNewReply)
                        .enqueue()
                }
                _navigateBack.value = true
            }
        }
    }
}

class NewThreadViewModelFactory(
    private val quotedPostId: String,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewReplyViewModel::class.java)) {
            return NewReplyViewModel(
                quotedPostId,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}