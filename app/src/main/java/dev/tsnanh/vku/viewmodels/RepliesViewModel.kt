/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.Post
import dev.tsnanh.vku.network.VKUServiceApi
import dev.tsnanh.vku.network.asDomainModel
import dev.tsnanh.vku.repository.VKURepository
import dev.tsnanh.vku.view.replies.POST_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class RepliesViewModel(
    private val threadId: String,
    application: Application
) : AndroidViewModel(application) {
    private val repository by inject(VKURepository::class.java)

    val thread = repository.getThreadById(threadId)

    private val _replies = MutableLiveData<List<Post>>()
    val replies: LiveData<List<Post>>
        get() = _replies

    init {
        refresh()
    }

    val createPostWorkerLiveData =
        WorkManager.getInstance(getApplication()).getWorkInfosByTagLiveData(POST_TAG)

    private suspend fun refreshReplies() {
        withContext(Dispatchers.IO) {
            _replies.postValue(VKUServiceApi.network.getRepliesInThread(threadId).asDomainModel())
        }
    }

    fun refresh() {
        viewModelScope.launch {
            refreshReplies()
        }
    }
}

class RepliesViewModelFactory(private val threadId: String, private val application: Application) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepliesViewModel::class.java)) {
            return RepliesViewModel(
                threadId,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}