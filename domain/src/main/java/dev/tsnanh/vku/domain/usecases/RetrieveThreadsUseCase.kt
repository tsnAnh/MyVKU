package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import javax.inject.Inject

interface RetrieveThreadsUseCase {
    fun invoke(forumId: String): LiveData<Resource<List<NetworkForumThread>>>
}

class RetrieveThreadsUseCaseImpl @Inject constructor(
    private val threadRepo: ThreadRepo
) : RetrieveThreadsUseCase {
    override fun invoke(forumId: String): LiveData<Resource<List<NetworkForumThread>>> =
        threadRepo.getThreads(forumId).asLiveData()
}