package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.ForumThread
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import org.koin.java.KoinJavaComponent.inject

interface CreateNewThreadUseCase {
    suspend fun execute(
        idToken: String,
        thread: ForumThread,
        forumId: String
    ): Resource<ForumThread>
}

class CreateNewThreadUseCaseImpl : CreateNewThreadUseCase {
    private val threadRepo by inject(ThreadRepo::class.java)
    override suspend fun execute(idToken: String, thread: ForumThread, forumId: String) =
        try {
            Resource.Success(VKUServiceApi.network.createThread(idToken, thread, forumId))
        } catch (e: Exception) {
            ErrorHandler.handleError<ForumThread>(e)
        }

}