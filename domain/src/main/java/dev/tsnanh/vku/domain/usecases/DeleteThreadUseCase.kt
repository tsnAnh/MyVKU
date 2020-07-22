package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import org.koin.java.KoinJavaComponent.inject

interface DeleteThreadUseCase {
    fun invoke(idToken: String, threadId: String): LiveData<String>
}

class DeleteThreadUseCaseImpl : DeleteThreadUseCase {
    private val threadRepo by inject(ThreadRepo::class.java)
    override fun invoke(idToken: String, threadId: String) = liveData {
        emit(threadRepo.deleteThread(idToken, threadId))
    }
}