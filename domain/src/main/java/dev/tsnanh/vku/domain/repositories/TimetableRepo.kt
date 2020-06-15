package dev.tsnanh.vku.domain.repositories

import androidx.lifecycle.LiveData
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import org.koin.java.KoinJavaComponent

interface TimetableRepo {
    suspend fun getTimetable(url: String, email: String): Resource<List<Subject>>
    fun getTimetableLiveData(): LiveData<List<Subject>>
    suspend fun refresh(url: String, email: String)
}

class TimetableRepoImpl : TimetableRepo {
    private val dao by KoinJavaComponent.inject(VKUDao::class.java)
    override fun getTimetableLiveData() = dao.getAllSubjectsLiveData()

    override suspend fun getTimetable(url: String, email: String): Resource<List<Subject>> {
        return try {
            this.refresh(url, email)
            Resource.Success(dao.getAllSubjects())
        } catch (e: Throwable) {
            ErrorHandler.handleError(e)
        }
    }

    @Throws(Exception::class)
    override suspend fun refresh(url: String, email: String) {
        try {
            val subjects = VKUServiceApi.network.getTimetable(url, email)
            dao.insertAllSubjects(*subjects.toTypedArray())
        } catch (e: Throwable) {
            throw e
        }
    }
}