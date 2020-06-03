/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.usecases.RetrieveSingleForumUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveThreadsUseCase
import org.koin.java.KoinJavaComponent.inject

class ThreadViewModel(forumId: String) : ViewModel() {
    private val retrieveThreadsUseCase by inject(RetrieveThreadsUseCase::class.java)
    private val retrieveSingleForumUseCase by inject(RetrieveSingleForumUseCase::class.java)
    val threads = retrieveThreadsUseCase.execute(forumId)
    val forum = retrieveSingleForumUseCase.execute(forumId)
    private val _navigateToReplies = MutableLiveData<Pair<ForumThread, MaterialCardView>>()
    val navigateToReplies: LiveData<Pair<ForumThread, MaterialCardView>>
        get() = _navigateToReplies

    fun onNavigateToReplies(thread: ForumThread, cardView: MaterialCardView) {
        _navigateToReplies.value = thread to cardView
    }

    fun onNavigatedToReplies() {
        _navigateToReplies.value = null
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
