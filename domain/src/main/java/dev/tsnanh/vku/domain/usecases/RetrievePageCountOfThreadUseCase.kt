package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrievePageCountOfThreadUseCase {
    fun invoke(threadId: String, limit: Int): LiveData<Resource<ReplyContainer>>
}

class RetrievePageCountOfThreadUseCaseImpl : RetrievePageCountOfThreadUseCase {
    private val replyRepo by inject(ReplyRepo::class.java)
    override fun invoke(threadId: String, limit: Int): LiveData<Resource<ReplyContainer>> {
        return replyRepo.getPageCount(threadId, limit).asLiveData()
    }
}