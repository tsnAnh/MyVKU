package dev.tsnanh.vku.domain.repositories

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NewsRepo {
    fun getNews(): Flow<List<News>>
    suspend fun refreshNews()
}

class NewsRepoImpl @Inject constructor(
    private val dao: VKUDao,
) : NewsRepo {
    @Throws(Exception::class)
    override suspend fun refreshNews() {
        try {
            withContext(Dispatchers.IO) {
                val jsonString = VKUServiceApi.network.getNews(
                    time = ""
                ).string()

                val moshi = Moshi.Builder().build()
                val listNews = Resource.Success(
                    moshi.adapter<List<News>>(
                        Types.newParameterizedType(
                            List::class.java,
                            News::class.java
                        )
                    ).fromJson(jsonString)
                ).data
                listNews?.toTypedArray()?.let { dao.insertAllNews(*it) }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getNews(): Flow<List<News>> {
        return dao.getAllNews()
    }
}