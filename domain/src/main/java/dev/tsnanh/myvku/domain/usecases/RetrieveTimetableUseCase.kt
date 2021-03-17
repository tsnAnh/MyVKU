package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.repositories.TimetableFilter
import dev.tsnanh.myvku.domain.repositories.TimetableRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveTimetableUseCase {
    fun invoke(email: String, type: TimetableFilter): Flow<State<List<Subject>>>
    suspend fun refresh(email: String)
}

class RetrieveTimetableUseCaseImpl @Inject constructor(
    private val timetableRepo: TimetableRepo
) : RetrieveTimetableUseCase {
    override fun invoke(email: String, type: TimetableFilter) =
        timetableRepo.getLocalTimetable(email, type)

    override suspend fun refresh(email: String) = timetableRepo.refresh(email)
}