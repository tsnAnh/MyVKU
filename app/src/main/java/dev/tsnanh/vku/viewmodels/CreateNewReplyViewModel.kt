/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.usecases.RetrieveReplyByIdUseCase

class CreateNewReplyViewModel @ViewModelInject constructor(
    private val retrieveReplyByIdUseCase: RetrieveReplyByIdUseCase,
    private val workManager: WorkManager,
) : ViewModel() {
    fun quotedReply(replyId: String?) = replyId?.let { retrieveReplyByIdUseCase.execute(it) }

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