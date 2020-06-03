package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.network.VKUServiceApi

interface RetrieveRepliesUseCase {
    suspend fun execute(threadId: String, page: Int, limit: Int): ReplyContainer
}

class RetrieveRepliesUseCaseImpl : RetrieveRepliesUseCase {
    override suspend fun execute(threadId: String, page: Int, limit: Int) =
        VKUServiceApi.network.getRepliesInThread(threadId, page, limit)
}