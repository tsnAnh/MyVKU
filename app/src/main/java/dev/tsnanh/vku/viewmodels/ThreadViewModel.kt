/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UpdateThreadBody
import dev.tsnanh.vku.domain.usecases.DeleteThreadUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveThreadsUseCase
import dev.tsnanh.vku.domain.usecases.UpdateThreadTitleUseCase

class ThreadViewModel @ViewModelInject constructor(
    private val retrieveThreadsUseCase: RetrieveThreadsUseCase,
    private val updateThreadTitleUseCase: UpdateThreadTitleUseCase,
    private val deleteThreadUseCase: DeleteThreadUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // Data source LiveData
    fun getThreads(forumId: String) = retrieveThreadsUseCase.invoke(forumId)

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
