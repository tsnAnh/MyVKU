package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.NetworkForumThread
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.UpdateThreadBody
import dev.tsnanh.vku.domain.repositories.ThreadRepo
import org.koin.java.KoinJavaComponent.inject

interface UpdateThreadTitleUseCase {
    fun invoke(
        idToken: String,
        threadId: String,
        body: UpdateThreadBody
    ): LiveData<Resource<NetworkForumThread>>
}

class UpdateThreadTitleUseCaseImpl : UpdateThreadTitleUseCase {
    private val threadRepo by inject(ThreadRepo::class.java)
    override fun invoke(
        idToken: String,
        threadId: String,
        body: UpdateThreadBody
    ) = threadRepo.updateThreadTitle(idToken, threadId, body).asLiveData()
}
