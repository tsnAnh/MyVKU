/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import androidx.lifecycle.*
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCase
import org.koin.java.KoinJavaComponent.inject

class NewReplyViewModel(quotedReplyId: String?, application: Application) :
    AndroidViewModel(application) {
    private val retrieveReplyUseCase by inject(RetrieveReplyByIdUseCase::class.java)

    val quotedReply = quotedReplyId?.let { retrieveReplyUseCase.execute(it) }

    private val workManager by inject(WorkManager::class.java)

    val createNewReplyWorkerData = workManager.getWorkInfosByTagLiveData("dev.tsnanh.newreply")
    private val _pickerHasImage = MutableLiveData(false)
    val pickerHasImage: LiveData<Boolean>
        get() = _pickerHasImage

    fun onPickerHasImage() {
        _pickerHasImage.value = true
    }

    fun onPickerHasNoImage() {
        _pickerHasImage.value = false
    }
}

@Suppress("UNCHECKED_CAST")
class NewReplyViewModelFactory(
    private val quotedReplyId: String? = null,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewReplyViewModel::class.java)) {
            return NewReplyViewModel(quotedReplyId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}