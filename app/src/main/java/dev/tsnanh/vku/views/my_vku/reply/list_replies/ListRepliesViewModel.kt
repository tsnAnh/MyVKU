package dev.tsnanh.vku.views.my_vku.reply.list_replies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesUseCase
import org.koin.java.KoinJavaComponent.inject

class ListRepliesViewModel(
    private val threadId: String,
    private val position: Int
) : ViewModel() {
    private val retrieveRepliesLiveDataUseCase by inject(RetrieveRepliesUseCase::class.java)
    private var _listReplies = retrieveRepliesLiveDataUseCase.execute(threadId, position, 10)
    val listReplies: LiveData<Resource<ReplyContainer>>
        get() = _listReplies

    fun refreshPage() {
        _listReplies = retrieveRepliesLiveDataUseCase.execute(threadId, position, 10)
    }
}

class ListRepliesViewModelFactory(
    private val threadId: String,
    private val position: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListRepliesViewModel::class.java)) {
            return ListRepliesViewModel(
                threadId,
                position
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
