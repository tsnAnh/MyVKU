/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.card.MaterialCardView
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UpdateThreadBody
import dev.tsnanh.vku.domain.usecases.DeleteThreadUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveThreadsUseCase
import dev.tsnanh.vku.domain.usecases.UpdateThreadTitleUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import timber.log.Timber

class ThreadViewModel @ViewModelInject constructor(
    private val retrieveThreadsUseCase: RetrieveThreadsUseCase,
    private val updateThreadTitleUseCase: UpdateThreadTitleUseCase,
    private val deleteThreadUseCase: DeleteThreadUseCase,
    private val client: GoogleSignInClient,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // Data source LiveData
    fun getThreads(forumId: String) = retrieveThreadsUseCase.invoke(forumId)

    // Functional LiveData
    private val _navigateToReplies =
        MutableLiveData<Pair<NetworkForumThread, MaterialCardView>>()
    val navigateToReplies: LiveData<Pair<NetworkForumThread, MaterialCardView>>
        get() = _navigateToReplies

    private val _updateThread = MutableLiveData<Resource<ForumThread>?>()
    val updateThread: LiveData<Resource<ForumThread>?>
        get() = _updateThread

    private val _deleteThread = MutableLiveData<Pair<Resource<String>, Int>?>()
    val deleteThread: LiveData<Pair<Resource<String>, Int>?>
        get() = _deleteThread

    fun onNavigateToReplies(thread: NetworkForumThread, cardView: MaterialCardView) {
        _navigateToReplies.value = thread to cardView
    }

    fun onNavigatedToReplies() {
        _navigateToReplies.value = null
    }

    private suspend fun getIdToken() = client.silentSignIn().asDeferred().await().idToken.also {
        Timber.i("Token created")
    }

    fun updateThreadTitle(
        threadId: String,
        newTitle: String,
    ) {
        viewModelScope.launch {
            val token = getIdToken()
            if (token != null) {
                updateThreadTitleUseCase.invoke(token, threadId, UpdateThreadBody(title = newTitle))
                    .asFlow()
                    .collectLatest {
                        _updateThread.value = it
                    }
            }
        }
    }

    fun deleteThread(threadId: String, itemId: Int) {
        viewModelScope.launch {
            val token = getIdToken()
            if (token != null) {
                deleteThreadUseCase.invoke(token, threadId)
                    .collectLatest { resource ->
                        _deleteThread.value = resource to itemId
                    }
            }
        }
    }

    fun onThreadUpdated() {
        _updateThread.value = null
    }

    fun onThreadDeleted() {
        _deleteThread.value = null
    }
}
