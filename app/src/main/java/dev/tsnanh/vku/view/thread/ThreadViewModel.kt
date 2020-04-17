/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.view.thread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.repository.VKURepository
import org.koin.java.KoinJavaComponent.inject

class ThreadViewModel(forumId: String) : ViewModel() {
    private val repository: VKURepository by inject(VKURepository::class.java)

    val threads = repository.getThreadsInForum(forumId)
    val forum = repository.getForumById(forumId)

    private val _navigateToReplies = MutableLiveData<ForumThread>()
    val navigateToReplies: LiveData<ForumThread>
        get() = _navigateToReplies

    fun onNavigateToReplies(thread: ForumThread) {
        _navigateToReplies.value = thread
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
