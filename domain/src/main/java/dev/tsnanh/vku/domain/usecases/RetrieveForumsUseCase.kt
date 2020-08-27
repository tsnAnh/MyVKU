package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ForumRepo
import javax.inject.Inject

interface RetrieveForumsUseCase {
    fun execute(): LiveData<Resource<List<NetworkCustomForum>>>
}

class RetrieveForumsUseCaseImpl @Inject constructor(
    private val forumRepo: ForumRepo
) : RetrieveForumsUseCase {
    override fun execute() =
        forumRepo
            .getForums()
            .asLiveData()
}