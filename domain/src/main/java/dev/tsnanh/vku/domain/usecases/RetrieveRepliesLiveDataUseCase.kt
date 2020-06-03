package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ForumRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveRepliesLiveDataUseCase {
    fun execute(threadId: String, position: Int, i1: Int): LiveData<Resource<ReplyContainer>>
}

class RetrieveRepliesLiveDataUseCaseImpl : RetrieveRepliesLiveDataUseCase {
    private val forumRepo by inject(ForumRepo::class.java)
    override fun execute(threadId: String, position: Int, i1: Int) = liveData {
        emit(Resource.Loading<ReplyContainer>())
        try {
            emit(Resource.Success(forumRepo.getReplies(threadId, position, i1)))
        } catch (e: Exception) {
            emit(Resource.Error<ReplyContainer>("Something went wrong!"))
        }
    }
}