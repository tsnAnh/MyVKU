/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.usecases.RetrieveForumsUseCase
import org.koin.java.KoinJavaComponent.inject

class NewThreadViewModel : ViewModel() {
    private val retrieveForumsUseCase by inject(RetrieveForumsUseCase::class.java)
    val forums = retrieveForumsUseCase.execute()

    private val workManager by inject(WorkManager::class.java)
    val newReplyWorkLiveData = workManager.getWorkInfosByTagLiveData("dev.tsnanh.newreply")

    private val _pickerHasImage = MutableLiveData(false)
    val pickerHasImage: LiveData<Boolean>
        get() = _pickerHasImage

    private val _navigateToReplyFragment = MutableLiveData<String>()
    val navigateToReplyFragment: LiveData<String>
        get() = _navigateToReplyFragment

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
