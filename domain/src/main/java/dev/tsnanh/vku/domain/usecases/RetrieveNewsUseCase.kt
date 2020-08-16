package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.NewsRepo
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

interface RetrieveNewsUseCase {
    fun execute(): Flow<List<News>>
    suspend fun refresh()
}

class RetrieveNewsUseCaseImpl : RetrieveNewsUseCase {
    private val newsRepo: NewsRepo by inject(NewsRepo::class.java)
    override fun execute() = newsRepo.getNews()
    override suspend fun refresh() {
        newsRepo.refreshNews()
    }
}