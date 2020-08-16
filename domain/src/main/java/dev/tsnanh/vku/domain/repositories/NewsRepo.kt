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
import org.koin.java.KoinJavaComponent.inject

interface NewsRepo {
    fun getNews(): Flow<List<News>>
    suspend fun refreshNews()
}

class NewsRepoImpl : NewsRepo {
    private val dao by inject(VKUDao::class.java)
    override suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            val jsonString = VKUServiceApi.network.getNews(
                "http://daotao.sict.udn.vn/baimoinhat",
                ""
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
    }

    override fun getNews(): Flow<List<News>> {
        return dao.getAllNews()
    }
}