package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.repositories.TimetableRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveUserTimetableLiveDataUseCase {
    fun invoke(): LiveData<List<Subject>>
    suspend fun refresh(email: String)
}

class RetrieveUserTimetableLiveDataUseCaseImpl : RetrieveUserTimetableLiveDataUseCase {
    private val timetableRepo by inject(TimetableRepo::class.java)
    override fun invoke(): LiveData<List<Subject>> {
        return timetableRepo.getTimetableLiveData()
    }

    @Throws(Exception::class)
    override suspend fun refresh(email: String) {
        try {
            timetableRepo.refreshSubjects(email)
        } catch (e: Throwable) {
            throw e
        }
    }
}

