package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.network.VKUServiceApi

interface CreateNewReplyUseCase {
    suspend fun execute(idToken: String, reply: Reply): Reply
}

class CreateNewReplyUseCaseImpl : CreateNewReplyUseCase {
    override suspend fun execute(idToken: String, reply: Reply) =
        VKUServiceApi.network.newReply(idToken, reply)
}