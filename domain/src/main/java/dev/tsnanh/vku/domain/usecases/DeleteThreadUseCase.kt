package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeleteThreadUseCase {
    fun invoke(idToken: String, threadId: String): Flow<Resource<String>>
}

class DeleteThreadUseCaseImpl @Inject constructor(
    private val threadRepo: ThreadRepo
) : DeleteThreadUseCase {
    override fun invoke(idToken: String, threadId: String) =
        threadRepo.deleteThread(idToken, threadId)
}