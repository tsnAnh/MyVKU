package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.tsnanh.vku.domain.entities.Forum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import dev.tsnanh.vku.domain.repositories.ForumRepo
import javax.inject.Inject

interface RetrieveSingleForumUseCase {
    fun execute(forumId: String): LiveData<Resource<Forum>>
}

class RetrieveSingleForumUseCaseImpl @Inject constructor(
    private val forumRepo: ForumRepo
) : RetrieveSingleForumUseCase {
    override fun execute(forumId: String): LiveData<Resource<Forum>> = liveData {

    }
}