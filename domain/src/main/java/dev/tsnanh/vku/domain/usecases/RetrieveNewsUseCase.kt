package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.repositories.NewsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveNewsUseCase {
    fun execute(): Flow<List<News>>
    suspend fun refresh()
}

class RetrieveNewsUseCaseImpl @Inject constructor(
    private val newsRepo: NewsRepo
) : RetrieveNewsUseCase {
    override fun execute() = newsRepo.getNews()
    override suspend fun refresh() {
        newsRepo.refreshNews()
    }
}