package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.NetworkForum
import dev.tsnanh.vku.domain.repositories.ForumRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveForumsUseCase {
    fun execute(): Flow<List<NetworkForum>>
}

class RetrieveForumsUseCaseImpl @Inject constructor(
    private val forumRepo: ForumRepo
) : RetrieveForumsUseCase {
    override fun execute() =
        forumRepo
            .getForums()
}