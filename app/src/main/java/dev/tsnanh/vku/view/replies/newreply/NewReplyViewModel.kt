/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.view.replies.newreply

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.domain.Resource
import dev.tsnanh.vku.domain.asNetworkModel
import dev.tsnanh.vku.network.NetworkPost
import dev.tsnanh.vku.repository.VKURepository
import dev.tsnanh.vku.view.replies.POST_TAG
import dev.tsnanh.vku.worker.CreateNewPostWorker
import dev.tsnanh.vku.worker.UploadPostImageWorker
import org.koin.java.KoinJavaComponent.inject

class NewReplyViewModel(quotedPostId: String, application: Application) :
    AndroidViewModel(application) {

    private val repo by inject(VKURepository::class.java)
    val quotedPost = if (quotedPostId.isNotEmpty()) {
        repo.getReplyById(quotedPostId)
    } else {
        MutableLiveData(Resource.Error<Post>("empty"))
    }

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

    fun newReply(post: Post, images: List<Uri>) {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val networkPost = post.asNetworkModel()
        val adapter =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(NetworkPost::class.java)
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
            return NewReplyViewModel(quotedPostId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}