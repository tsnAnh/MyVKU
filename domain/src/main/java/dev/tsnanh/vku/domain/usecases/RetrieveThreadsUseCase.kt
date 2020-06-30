package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveThreadsUseCase {
    fun invoke(forumId: String): LiveData<Resource<List<NetworkForumThreadCustom>>>
}

class RetrieveThreadsUseCaseImpl : RetrieveThreadsUseCase {
    private val threadRepo by inject(ThreadRepo::class.java)
    override fun invoke(forumId: String): LiveData<Resource<List<NetworkForumThreadCustom>>> =
        threadRepo.getThreads(forumId).asLiveData()
}