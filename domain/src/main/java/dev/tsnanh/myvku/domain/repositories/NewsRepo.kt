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
}

class NewsRepoImpl @Inject constructor(
    private val dao: VKUDao,
) : NewsRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLatestNews() = flow {
        emit(State.loading())
        val news = VKUServiceApi.network.getNews(time = "")
        if (news[0] == dao.getLatestNews()) {
            emitAll(dao.getAllNews().mapLatest { State.success(it) })
            return@flow
        }
        dao.insertAllNews(*news.toTypedArray())
        emit(State.success(news))
    }.catch { t ->
        emit(State.error(t))
    }.flowOn(Dispatchers.IO)
}