package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.News
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.repositories.NewsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveNewsUseCase {
    fun execute(): Flow<State<List<News>>>
}

class RetrieveNewsUseCaseImpl @Inject constructor(
    private val newsRepo: NewsRepo
) : RetrieveNewsUseCase {
    override fun execute(): Flow<State<List<News>>> = newsRepo.getLatestNews()
}