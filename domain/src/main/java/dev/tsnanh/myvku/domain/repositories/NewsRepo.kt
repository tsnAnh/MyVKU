package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.entities.News
import dev.tsnanh.myvku.domain.entities.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NewsRepo {
    fun getLatestNews(): Flow<State<List<News>>>
}

class NewsRepoImpl @Inject constructor(
    private val dao: VKUDao,
) : NewsRepo {

    override fun getLatestNews(): Flow<State<List<News>>> {
        return flow {
            emit(State.success(emptyList()))
        }
    }
}