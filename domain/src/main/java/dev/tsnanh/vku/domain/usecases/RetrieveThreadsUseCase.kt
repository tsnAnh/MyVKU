package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.ThreadContainer
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveThreadsUseCase {
    fun execute(forumId: String): LiveData<Resource<ThreadContainer>>
}

class RetrieveThreadsUseCaseImpl : RetrieveThreadsUseCase {
    private val threadRepo by inject(ThreadRepo::class.java)
    override fun execute(forumId: String) = liveData {
        emit(Resource.Loading<ThreadContainer>())
        try {
            emit(Resource.Success(threadRepo.getThreads(forumId)))
        } catch (e: Exception) {
            emit(ErrorHandler.handleError<ThreadContainer>(e))
        }
    }
}