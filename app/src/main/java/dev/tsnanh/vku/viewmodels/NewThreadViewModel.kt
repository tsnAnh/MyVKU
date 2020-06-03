/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.usecases.RetrieveForumsUseCase
import org.koin.java.KoinJavaComponent.inject

const val TAG_NEW_THREAD = "dev.tsnanh.newthread"

class NewThreadViewModel(application: Application) : AndroidViewModel(application) {
    private val retrieveForumsUseCase by inject(RetrieveForumsUseCase::class.java)
    val forums = retrieveForumsUseCase.execute()

    val createThreadWorkerLiveData =
        WorkManager.getInstance(getApplication()).getWorkInfosByTagLiveData(TAG_NEW_THREAD)

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
