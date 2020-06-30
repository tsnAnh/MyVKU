package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UserPopulatedNetworkReply
import dev.tsnanh.vku.domain.repositories.ReplyRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveReplyByIdUseCase {
    fun execute(replyId: String): LiveData<Resource<UserPopulatedNetworkReply>>
}

class RetrieveReplyByIdUseCaseImpl : RetrieveReplyByIdUseCase {
    private val replyRepo by inject(ReplyRepo::class.java)
    override fun execute(replyId: String): LiveData<Resource<UserPopulatedNetworkReply>> =
        replyRepo.getReplyById(replyId).asLiveData()
}