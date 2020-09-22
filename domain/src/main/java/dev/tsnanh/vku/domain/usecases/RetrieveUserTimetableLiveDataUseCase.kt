package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.repositories.TimetableRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveUserTimetableLiveDataUseCase {
    fun invoke(): LiveData<List<Subject>>
    fun filter(type: Int): Flow<List<Subject>>
    suspend fun refresh(email: String)
}

class RetrieveUserTimetableLiveDataUseCaseImpl @Inject constructor(
    private val timetableRepo: TimetableRepo
) : RetrieveUserTimetableLiveDataUseCase {
    override fun invoke(): LiveData<List<Subject>> {
        return timetableRepo.getTimetableLiveData()
    }

    @Throws(Throwable::class)
    override suspend fun refresh(email: String) {
        try {
            timetableRepo.refreshSubjects(email)
        } catch (e: Throwable) {
            throw e
        }
    }

    override fun filter(type: Int): Flow<List<Subject>> {
        return timetableRepo.getTimetableWithFilter(type)
    }
}

