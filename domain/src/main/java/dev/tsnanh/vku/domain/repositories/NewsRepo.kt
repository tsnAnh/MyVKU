package dev.tsnanh.vku.domain.repositories

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface NewsRepo {
    fun getNews(): Flow<List<News>>
    suspend fun refreshNews()
    fun getLatestNews(): Flow<List<News>>
}

class NewsRepoImpl @Inject constructor(
    private val dao: VKUDao,
) : NewsRepo {

    @Throws(Exception::class)
    override suspend fun refreshNews() {
        try {
            val type = Types.newParameterizedType(
                List::class.java,
                News::class.java
            )
            withContext(Dispatchers.IO) {
                val jsonString = VKUServiceApi.network.getNews(
                    time = ""
                ).string()

                val moshi = Moshi.Builder().build()
                val listNews = Resource.Success(
                    moshi.adapter<List<News>>(type).fromJson(jsonString)
                ).data
                listNews?.toTypedArray()?.let { dao.insertAllNews(*it) }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getLatestNews(): Flow<List<News>> {
        val type = Types.newParameterizedType(
            List::class.java,
            News::class.java
        )
        return flow {
            val calendar = Calendar.getInstance()
            val time =
                "${calendar[Calendar.YEAR]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.DATE]}"
            val jsonString = VKUServiceApi.network.getNews(
                time = time
            ).string()

            val moshi = Moshi.Builder().build()

            val listNews = Resource.Success(
                moshi.adapter<List<News>>(type).fromJson(jsonString)
            ).data
            emit(listNews!!)
        }
    }

    override fun getNews(): Flow<List<News>> {
        return dao.getAllNews()
    }


}