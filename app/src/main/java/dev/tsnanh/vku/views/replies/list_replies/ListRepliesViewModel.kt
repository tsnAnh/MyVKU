package dev.tsnanh.vku.views.replies.list_replies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.tsnanh.vku.domain.usecases.RetrieveRepliesLiveDataUseCase
import org.koin.java.KoinJavaComponent.inject

class ListRepliesViewModel(
    threadId: String,
    position: Int
) : ViewModel() {
    private val retrieveRepliesLiveDataUseCase by inject(RetrieveRepliesLiveDataUseCase::class.java)
    val listReplies = retrieveRepliesLiveDataUseCase.execute(threadId, position, 10)
}

class ListRepliesViewModelFactory(
    private val threadId: String,
    private val position: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListRepliesViewModel::class.java)) {
            return ListRepliesViewModel(threadId, position) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
