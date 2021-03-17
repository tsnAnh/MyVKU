package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.entities.News
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

interface NewsRepo {
    fun getLatestNews(): Flow<State<List<News>>>
    suspend fun refresh()
}

class NewsRepoImpl @Inject constructor(
    private val dao: VKUDao,
) : NewsRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLatestNews() = flow {
        emit(State.loading())
        emitAll(dao.getAllNewsFlow().mapLatest { State.success(it) })
    }.catch { emit(State.error(it)) }.flowOn(Dispatchers.IO)

    override suspend fun refresh() {
        val news = VKUServiceApi.network.getNews()
        val subList = news.sortedByDescending { it.updatedDate }.subList(0, 10)
        val localNews = dao.getAllNewsWithLimit()
        if (localNews.isEmpty() || (!subList.containsAll(localNews) && !localNews.containsAll(subList))) {
            dao.insertAllNews(*news.toTypedArray())
        }
    }
}