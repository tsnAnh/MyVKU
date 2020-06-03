/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.usecases.CreateNewReplyUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCase
import dev.tsnanh.vku.views.replies.POST_TAG
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
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(true)?.addOnSuccessListener {
            it.token?.let { token ->
                val createNewReply = OneTimeWorkRequestBuilder<CreateNewPostWorker>()
                    .setConstraints(constraint)
                    .setInputMerger(ArrayCreatingInputMerger::class.java)
                    .addTag(POST_TAG)
                    .setInputData(
                        workDataOf(
                            "id_token" to token,
                            "post" to json
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
                                    "image" to uri.toString(),
                                    "id_token" to token,
                                    "uid" to user.uid
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