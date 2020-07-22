package dev.tsnanh.vku.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCase
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.workers.UpdateReplyWorker
import org.koin.java.KoinJavaComponent.inject

class UpdateReplyViewModel(
    private val replyId: String
) : ViewModel() {
    // Use Case
    private val retrieveReplyUseCase by inject(RetrieveReplyByIdUseCase::class.java)

    // Work Manager instance
    private val workManager by inject(WorkManager::class.java)

    // GoogleSignIn client
    private val client by inject(GoogleSignInClient::class.java)

    val reply = retrieveReplyUseCase.execute(replyId)
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
        newImage: List<Uri>? = null,
        content: String,
        images: List<String>? = null
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

class UpdateReplyViewModelFactory(
    private val replyId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateReplyViewModel::class.java)) {
            return UpdateReplyViewModel(replyId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}