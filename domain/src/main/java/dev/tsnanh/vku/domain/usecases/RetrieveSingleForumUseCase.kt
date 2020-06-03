package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.entities.Forum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.network.VKUServiceApi

interface RetrieveSingleForumUseCase {
    fun execute(forumId: String): LiveData<Resource<Forum>>
}

class RetrieveSingleForumUseCaseImpl : RetrieveSingleForumUseCase {
    override fun execute(forumId: String) = liveData {
        emit(Resource.Loading<Forum>())
        try {
            emit(Resource.Success(VKUServiceApi.network.getForumById(forumId)))
        } catch (e: Exception) {
            emit(Resource.Error<Forum>("CC"))
        }
    }
}