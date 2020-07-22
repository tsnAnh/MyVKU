package dev.tsnanh.vku.domain.repositories

import androidx.lifecycle.LiveData
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent

interface TimetableRepo {
    fun getTimetableLiveData(): LiveData<List<Subject>>
    suspend fun refreshSubjects(email: String)
    suspend fun getTimetable(email: String): Resource<List<Subject>>
}

class TimetableRepoImpl : TimetableRepo {
    private val dao by KoinJavaComponent.inject(VKUDao::class.java)

    override fun getTimetableLiveData(): LiveData<List<Subject>> {
        return dao.getAllSubjectsLiveData()
    }

    override suspend fun refreshSubjects(email: String) {
        withContext(Dispatchers.IO) {
            val subjects =
                VKUServiceApi.network.getTimetable("http://daotao.sict.udn.vn/tkb", email)
            dao.insertAllSubjects(*subjects.toTypedArray())
        }
    }

    override suspend fun getTimetable(email: String): Resource<List<Subject>> {
        return try {
            Resource.Success(dao.getAllSubjects())
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
        }
    }
}