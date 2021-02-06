package dev.tsnanh.myvku.domain.repositories

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.entities.News
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

interface NewsRepo {
    fun getLatestNews(): Flow<State<List<News>>>
}

class NewsRepoImpl @Inject constructor(
    private val dao: VKUDao,
    private val moshi: Moshi,
) : NewsRepo {

    override fun getLatestNews(): Flow<State<List<News>>> {
        val type = Types.newParameterizedType(
            List::class.java,
            News::class.java
        )
        return flow {
            emit(State.Loading())
            val calendar = Calendar.getInstance()
            val time =
                "${calendar[Calendar.YEAR]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.DATE]}"
            val jsonString = VKUServiceApi.network.getNews(
                time = time
            ).string()

            val listNews = State.Success(
                moshi.adapter<List<News>>(type).fromJson(jsonString)
            ).data
            dao.insertAllNews(*listNews!!.toTypedArray())
            emit(State.Success(listNews))
        }.catch { t ->
            when (t) {
                is SocketException, is SocketTimeoutException, is ConnectException, is UnknownHostException -> {
                    emit(State.error(t, dao.getAllNews().first()))
                }
                else -> emit(State.error(t))
            }
        }.flowOn(Dispatchers.IO)
    }
}