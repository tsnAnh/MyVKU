package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.repositories.NewsRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveNewsUseCase {
    fun execute(): LiveData<List<News>>
    suspend fun refresh()
}

class RetrieveNewsUseCaseImpl : RetrieveNewsUseCase {
    private val newsRepo: NewsRepo by inject(NewsRepo::class.java)
    override fun execute() = newsRepo.getAllNews()
    override suspend fun refresh() {
        newsRepo.refresh()
    }
}