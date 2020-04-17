/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.view.replies.newreply

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.domain.asNetworkModel
import dev.tsnanh.vku.network.NetworkPost
import dev.tsnanh.vku.worker.CreateNewPostWorker
import dev.tsnanh.vku.worker.UploadPostImageWorker

class NewReplyViewModel(application: Application) : AndroidViewModel(application) {

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
                    .addTag("create_post")
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
