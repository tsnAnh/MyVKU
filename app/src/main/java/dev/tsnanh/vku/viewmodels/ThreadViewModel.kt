/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveThreadsUseCase
import org.koin.java.KoinJavaComponent.inject

class ThreadViewModel(private val forumId: String) : ViewModel() {
    // Use case
    private val retrieveThreadsUseCase by inject(RetrieveThreadsUseCase::class.java)

    // Data source livedata
    private var _threads = retrieveThreadsUseCase.invoke(forumId)

    val threads: LiveData<Resource<List<NetworkForumThreadCustom>>>
        get() = _threads

    // Functional livedata
    private val _navigateToReplies =
        MutableLiveData<Pair<NetworkForumThreadCustom, MaterialCardView>>()
    val navigateToReplies: LiveData<Pair<NetworkForumThreadCustom, MaterialCardView>>
        get() = _navigateToReplies

    fun onNavigateToReplies(thread: NetworkForumThreadCustom, cardView: MaterialCardView) {
        _navigateToReplies.value = thread to cardView
    }

    fun onNavigatedToReplies() {
        _navigateToReplies.value = null
    }

    fun refreshThreads() {
        _threads = retrieveThreadsUseCase.invoke(forumId)
    }
}

class ThreadViewModelFactory(
    private val forumId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThreadViewModel::class.java)) {
            return ThreadViewModel(forumId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
