package dev.tsnanh.vku.domain.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

interface NewsRepo {
    fun getAllNews(): LiveData<List<News>>
    suspend fun refresh()
}

class NewsRepoImpl : NewsRepo {
    private val dao by inject(VKUDao::class.java)
    override fun getAllNews(): LiveData<List<News>> {
        return dao.getAllNews().asLiveData()
    }

    @Throws(Exception::class)
    override suspend fun refresh() = withContext(Dispatchers.IO) {
        try {
            // val news = VKUServiceApi.network.getNews
            // dao.insertAllNews(*news.news.toTypedArray())
        } catch (e: Throwable) {
            throw e
        }
    }
}