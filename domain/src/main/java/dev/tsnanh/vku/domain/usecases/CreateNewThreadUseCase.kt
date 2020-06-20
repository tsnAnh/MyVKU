package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.entities.CreateThreadContainer
import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import org.koin.java.KoinJavaComponent.inject

interface CreateNewThreadUseCase {
    fun execute(
        idToken: String,
        threadContainer: CreateThreadContainer,
        forumId: String
    ): LiveData<Resource<ForumThread>>
}

class CreateNewThreadUseCaseImpl : CreateNewThreadUseCase {
    private val threadRepo by inject(ThreadRepo::class.java)
    override fun execute(idToken: String, threadContainer: CreateThreadContainer, forumId: String) =
        liveData {
            emit(Resource.Loading<ForumThread>())
            try {
                emit(Resource.Success(threadRepo.createThread(idToken, threadContainer, forumId)))
            } catch (e: Exception) {
                emit(Resource.Error<ForumThread>(""))
            }
        }
}