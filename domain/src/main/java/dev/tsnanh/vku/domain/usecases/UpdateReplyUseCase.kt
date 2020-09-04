package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.entities.WorkResult
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

interface UpdateReplyUseCase {
    suspend fun invoke(
        idToken: String,
        replyId: String,
        newImage: Array<MultipartBody.Part>? = null,
        content: RequestBody,
        images: Array<RequestBody>? = null,
    ): WorkResult<Reply>
}

class UpdateReplyUseCaseImpl @Inject constructor(
    private val replyRepo: ReplyRepo
) : UpdateReplyUseCase {
    override suspend fun invoke(
        idToken: String,
        replyId: String,
        newImage: Array<MultipartBody.Part>?,
        content: RequestBody,
        images: Array<RequestBody>?
    ) = replyRepo.updateReply(idToken, replyId, newImage, content, images)
}