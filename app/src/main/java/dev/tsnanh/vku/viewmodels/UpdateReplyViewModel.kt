package dev.tsnanh.vku.viewmodels

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UserPopulatedNetworkReply
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCase
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.workers.UpdateReplyWorker

class UpdateReplyViewModel @ViewModelInject constructor(
    retrieveReplyUseCase: RetrieveReplyByIdUseCase,
    private val client: GoogleSignInClient,
    private val workManager: WorkManager,
) : ViewModel() {
    val reply: (String) -> LiveData<Resource<UserPopulatedNetworkReply>> =
        { retrieveReplyUseCase.execute(it) }
    private val _pickerHasImage = MutableLiveData(false)
    val pickerHasImage: LiveData<Boolean>
        get() = _pickerHasImage

    fun onPickerHasImage() {
        _pickerHasImage.value = true
    }

    fun onPickerHasNoImage() {
        _pickerHasImage.value = false

    }

    fun editReply(
        replyId: String,
        newImage: List<Uri>? = null,
        content: String,
        images: List<String>? = null,
    ) {
        client.silentSignIn().addOnSuccessListener { account ->
            // Token
            val idToken = account.idToken

            val newImageJson = newImage?.map {
                it.toString()
            }?.toTypedArray()
            val imagesJson = images?.toTypedArray()

            // Work Manager
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workData = workDataOf(
                Constants.TOKEN_KEY to idToken,
                "replyId" to replyId,
                "newImage" to newImageJson,
                "content" to content,
                "images" to imagesJson
            )
            val request = OneTimeWorkRequestBuilder<UpdateReplyWorker>()
                .setInputData(workData)
                .setConstraints(constraints)
                .addTag(UPDATE_REPLY_TAG)
                .build()
            workManager.enqueue(request)
        }
    }

    val updateReplyWorkLiveData = workManager.getWorkInfosByTagLiveData(UPDATE_REPLY_TAG)

}

private const val UPDATE_REPLY_TAG = "updateReply"