package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveRepliesUseCase {
    fun execute(threadId: String, page: Int, limit: Int): LiveData<Resource<ReplyContainer>>
}

class RetrieveRepliesUseCaseImpl : RetrieveRepliesUseCase {
    private val replyRepo by inject(ReplyRepo::class.java)
    override fun execute(threadId: String, page: Int, limit: Int) =
        replyRepo.getAllRepliesInThread(threadId, page, limit).asLiveData()
}