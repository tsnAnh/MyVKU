package dev.tsnanh.vku.view.replies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.tsnanh.vku.repository.VKURepository
import org.koin.java.KoinJavaComponent.inject

class RepliesViewModel(
    threadId: String
) : ViewModel() {
    private val repository by inject(VKURepository::class.java)

    val thread = repository.getThreadById(threadId)

    val replies = repository.getReplies(threadId)
}

class RepliesViewModelFactory(private val threadId: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepliesViewModel::class.java)) {
            return RepliesViewModel(threadId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}