package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import javax.inject.Inject

interface RetrieveRepliesUseCase {
    fun execute(threadId: String, page: Int, limit: Int): LiveData<Resource<ReplyContainer>>
}

class RetrieveRepliesUseCaseImpl @Inject constructor(
    private val replyRepo: ReplyRepo
) : RetrieveRepliesUseCase {
    override fun execute(threadId: String, page: Int, limit: Int) =
        replyRepo.getAllRepliesInThread(threadId, page, limit).asLiveData()
}