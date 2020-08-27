package dev.tsnanh.vku.domain.repositories

import androidx.lifecycle.LiveData
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TimetableRepo {
    fun getTimetableLiveData(): LiveData<List<Subject>>
    suspend fun refreshSubjects(email: String)
    suspend fun getTimetable(email: String): Resource<List<Subject>>
}

class TimetableRepoImpl @Inject constructor(
    private val dao: VKUDao
) : TimetableRepo {
    override fun getTimetableLiveData(): LiveData<List<Subject>> {
        return dao.getAllSubjectsLiveData()
    }

    override suspend fun refreshSubjects(email: String) {
        withContext(Dispatchers.IO) {
            val subjects =
                VKUServiceApi.network.getTimetable(email = email)
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