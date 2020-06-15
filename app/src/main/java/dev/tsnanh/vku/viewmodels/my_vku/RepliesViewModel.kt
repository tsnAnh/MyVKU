/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import androidx.lifecycle.*
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesLiveDataUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesUseCase
import dev.tsnanh.vku.utils.Constants.Companion.POST_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class RepliesViewModel(
    private val threadId: String,
    application: Application
) : AndroidViewModel(application) {
    private val retrieveRepliesLiveDataUseCase by inject(RetrieveRepliesLiveDataUseCase::class.java)
    private val retrieveRepliesUseCase by inject(RetrieveRepliesUseCase::class.java)

    val thread = retrieveRepliesLiveDataUseCase.execute(threadId, 1, 10)

    private val _replies = MutableLiveData<Pair<ReplyContainer, Boolean>>()
    val replies: LiveData<Pair<ReplyContainer, Boolean>>
        get() = _replies

    init {
        refresh(false)
    }

    val createPostWorkerLiveData =
        WorkManager.getInstance(getApplication()).getWorkInfosByTagLiveData(POST_TAG)

    private suspend fun refreshReplies(lastPage: Boolean) {
        withContext(Dispatchers.IO) {
            _replies.postValue(
                Pair(
                    retrieveRepliesUseCase.execute(threadId, 1, 10),
                    lastPage
                )
            )
        }
    }

    fun refresh(lastPage: Boolean) {
        viewModelScope.launch {
            refreshReplies(lastPage)
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