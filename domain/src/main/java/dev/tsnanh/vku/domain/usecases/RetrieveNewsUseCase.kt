package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.NewsRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveNewsUseCase {
    fun execute(url: String, time: String): LiveData<Resource<List<News>>>
}

class RetrieveNewsUseCaseImpl : RetrieveNewsUseCase {
    private val newsRepo: NewsRepo by inject(NewsRepo::class.java)
    override fun execute(url: String, time: String) = newsRepo.getNews(url, time).asLiveData()
}