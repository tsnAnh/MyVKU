package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import javax.inject.Inject

interface CreateNewThreadUseCase {
    suspend fun execute(
        idToken: String,
        thread: ForumThread,
        forumId: String
    ): Resource<ForumThread>
}

class CreateNewThreadUseCaseImpl @Inject constructor(
    private val threadRepo: ThreadRepo
) : CreateNewThreadUseCase {
    override suspend fun execute(idToken: String, thread: ForumThread, forumId: String) =
        try {
            Resource.Success(threadRepo.createThread(idToken, thread, forumId))
        } catch (e: Exception) {
            Resource.Error(e)
        }

}