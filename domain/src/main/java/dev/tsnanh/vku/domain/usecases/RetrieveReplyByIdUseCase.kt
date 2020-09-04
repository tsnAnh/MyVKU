package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.Reply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import javax.inject.Inject

interface RetrieveReplyByIdUseCase {
    fun execute(replyId: String): LiveData<Resource<Reply>>
}

class RetrieveReplyByIdUseCaseImpl @Inject constructor(
    private val replyRepo: ReplyRepo
) : RetrieveReplyByIdUseCase {
    override fun execute(replyId: String): LiveData<Resource<Reply>> =
        replyRepo.getReplyById(replyId).asLiveData()
}