package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import javax.inject.Inject

interface DeleteThreadUseCase {
    fun invoke(idToken: String, threadId: String): LiveData<String>
}

class DeleteThreadUseCaseImpl @Inject constructor(
    private val threadRepo: ThreadRepo
) : DeleteThreadUseCase {
    override fun invoke(idToken: String, threadId: String) = liveData {
        emit(threadRepo.deleteThread(idToken, threadId))
    }
}