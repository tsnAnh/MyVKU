package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.NetworkReply
import dev.tsnanh.vku.domain.entities.WorkResult
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent.inject

interface UpdateReplyUseCase {
    suspend fun invoke(
        idToken: String,
        replyId: String,
        newImage: Array<MultipartBody.Part>? = null,
        content: RequestBody,
        images: Array<RequestBody>? = null
    ): WorkResult<NetworkReply>
}

class UpdateReplyUseCaseImpl : UpdateReplyUseCase {
    private val replyRepo by inject(ReplyRepo::class.java)
    override suspend fun invoke(
        idToken: String,
        replyId: String,
        newImage: Array<MultipartBody.Part>?,
        content: RequestBody,
        images: Array<RequestBody>?
    ) = replyRepo.updateReply(idToken, replyId, newImage, content, images)
}