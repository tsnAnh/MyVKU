/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UpdateThreadBody
import dev.tsnanh.vku.domain.usecases.DeleteThreadUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveThreadsUseCase
import dev.tsnanh.vku.domain.usecases.UpdateThreadTitleUseCase
import org.koin.java.KoinJavaComponent.inject

class ThreadViewModel(private val forumId: String) : ViewModel() {
    // Use case
    private val retrieveThreadsUseCase by inject(RetrieveThreadsUseCase::class.java)
    private val deleteThreadUseCase by inject(DeleteThreadUseCase::class.java)
    private val updateThreadTitleUseCase by inject(UpdateThreadTitleUseCase::class.java)

    // Data source LiveData
    private var _threads = retrieveThreadsUseCase.invoke(forumId)
    val threads: LiveData<Resource<List<NetworkForumThreadCustom>>>
        get() = _threads

    // Functional LiveData
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
        _threads = retrieveThreadsUseCase
            .invoke(forumId)
    }

    fun refreshThreadsLiveData(): LiveData<Resource<List<NetworkForumThreadCustom>>> {
        return retrieveThreadsUseCase
            .invoke(forumId)
    }

    fun updateThreadTitle(
        idToken: String,
        threadId: String,
        body: UpdateThreadBody
    ): LiveData<Resource<NetworkForumThread>> {
        return updateThreadTitleUseCase.invoke(idToken, threadId, body)
    }

    fun deleteThread(idToken: String, threadId: String): LiveData<String> {
        return deleteThreadUseCase.invoke(idToken, threadId)
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
