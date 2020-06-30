package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.NetworkCustomForum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.ForumRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveForumsUseCase {
    fun execute(): LiveData<Resource<List<NetworkCustomForum>>>
}

class RetrieveForumsUseCaseImpl : RetrieveForumsUseCase {
    private val forumRepo by inject(ForumRepo::class.java)
    override fun execute() =
        forumRepo
            .getForums()
            .asLiveData()
}