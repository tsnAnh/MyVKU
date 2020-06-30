/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrievePageCountOfThreadUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesUseCase
import org.koin.java.KoinJavaComponent.inject

class RepliesViewModel(
    private val threadId: String,
    application: Application
) : AndroidViewModel(application) {
    private val retrieveRepliesUseCase by inject(RetrieveRepliesUseCase::class.java)
    private val workManager by inject(WorkManager::class.java)
    private var _allReplies: LiveData<Resource<ReplyContainer>> =
        retrieveRepliesUseCase.execute(threadId, 1, 10)
    val allReplies: LiveData<Resource<ReplyContainer>>
        get() = _allReplies

    private val retrievePageCountUseCase by inject(RetrievePageCountOfThreadUseCase::class.java)
    val pageCount = retrievePageCountUseCase.invoke(threadId, 10)
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