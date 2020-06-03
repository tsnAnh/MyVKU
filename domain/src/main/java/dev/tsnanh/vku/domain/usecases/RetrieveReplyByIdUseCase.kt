package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.network.VKUServiceApi

interface RetrieveReplyByIdUseCase {
    suspend fun execute(replyId: String): Reply
}

class RetrieveReplyByIdUseCaseImpl : RetrieveReplyByIdUseCase {
    override suspend fun execute(replyId: String) = VKUServiceApi.network.getReplyById(replyId)
}