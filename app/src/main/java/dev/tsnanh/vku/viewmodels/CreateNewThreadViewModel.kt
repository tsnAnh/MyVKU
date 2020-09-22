/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.os.Bundle
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.usecases.RetrieveForumsUseCase

class CreateNewThreadViewModel @ViewModelInject constructor(
    retrieveForumsUseCase: RetrieveForumsUseCase,
    workManager: WorkManager,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    val forums = retrieveForumsUseCase.execute()
    val newReplyWorkLiveData = workManager.getWorkInfosByTagLiveData("dev.tsnanh.newreply")

    private val _pickerHasImage = MutableLiveData(false)
    val pickerHasImage: LiveData<Boolean>
        get() = _pickerHasImage

    private val _navigateToReplyFragment = MutableLiveData<String>()
    val navigateToReplyFragment: LiveData<String>
        get() = _navigateToReplyFragment

    val forumSaveState: LiveData<Bundle>? = savedStateHandle["forum"]

    fun onPickerHasImage() {
        _pickerHasImage.value = true
    }

    fun onPickerHasNoImage() {
        _pickerHasImage.value = false
    }

    fun onNavigateToReplyFragment(threadId: String?) {
        _navigateToReplyFragment.value = threadId
    }

    fun onNavigatedToReplyFragment() {
        _navigateToReplyFragment.value = null
    }
}
