package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.repositories.TimetableFilter
import dev.tsnanh.myvku.domain.repositories.TimetableRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveUserTimetableUseCase {
    fun invoke(email: String, type: TimetableFilter): Flow<State<List<Subject>>>
}

class RetrieveUserTimetableUseCaseImpl @Inject constructor(
    private val timetableRepo: TimetableRepo
) : RetrieveUserTimetableUseCase {
    override fun invoke(email: String, type: TimetableFilter) =
        timetableRepo.getTimetable(email, type)
}