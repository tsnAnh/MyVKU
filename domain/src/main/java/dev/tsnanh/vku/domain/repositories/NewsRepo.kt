package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NewsRepo {
    fun getNews(url: String, time: String): Flow<Resource<List<News?>>>
}

class NewsRepoImpl : NewsRepo {
    override fun getNews(url: String, time: String): Flow<Resource<List<News?>>> = flow {
        emit(Resource.Loading())
        try {
        } catch (e: Exception) {
            emit(ErrorHandler.handleError(e))
        }
    }
}