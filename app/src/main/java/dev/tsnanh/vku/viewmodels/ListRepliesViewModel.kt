package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesUseCase

class ListRepliesViewModel @ViewModelInject constructor(
    private val retrieveRepliesUseCase: RetrieveRepliesUseCase,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun refreshPage(threadId: String, position: Int): LiveData<Resource<ReplyContainer>> {
        return retrieveRepliesUseCase.execute(threadId, position, 10)
    }
}