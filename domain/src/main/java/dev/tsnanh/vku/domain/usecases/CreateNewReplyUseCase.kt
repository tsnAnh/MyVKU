package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent.inject

interface CreateNewReplyUseCase {
    suspend fun execute(
        idToken: String,
        threadId: String,
        content: RequestBody,
        images: Array<MultipartBody.Part>? = null,
        quoted: RequestBody? = null
    ): Resource<Reply>
}

class CreateNewReplyUseCaseImpl : CreateNewReplyUseCase {
    private val replyRepo by inject(ReplyRepo::class.java)
    override suspend fun execute(
        idToken: String, threadId: String, content: RequestBody,
        images: Array<MultipartBody.Part>?,
        quoted: RequestBody?
    ) = replyRepo.createReply(idToken, threadId, content, images, quoted)
}