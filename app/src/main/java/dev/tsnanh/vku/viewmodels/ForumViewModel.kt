/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkForum
import dev.tsnanh.vku.domain.usecases.RetrieveForumsUseCase
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

class ForumViewModel @ViewModelInject constructor(
    private val retrieveForumsUseCase: RetrieveForumsUseCase,
) : ViewModel() {
    private val loadTrigger = MutableLiveData(Unit)

    fun refresh() {
        loadTrigger.value = Unit
    }

    private val _navigateToListThread =
        MutableLiveData<Pair<NetworkForum, MaterialCardView>?>()
    val navigateToListThread: LiveData<Pair<NetworkForum, MaterialCardView>?>
        get() = _navigateToListThread

    private val _error = MutableLiveData<Throwable?>()
    val error: LiveData<Throwable?>
        get() = _error

    val forums = loadTrigger.switchMap {
        retrieveForumsUseCase.execute()
    }

    fun onItemClick(forum: Pair<NetworkForum, MaterialCardView>) {
        _navigateToListThread.value = forum
    }

    fun onItemClicked() {
        _navigateToListThread.value = null
    }

    fun onError(t: Throwable?) {
        _error.value = t
    }

    fun clearError() {
        _error.value = null
    }
}
