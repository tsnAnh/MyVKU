package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.repositories.TimetableRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveUserTimetableUseCase {
    fun invoke(email: String): Flow<State<List<Subject>>>
}

class RetrieveUserTimetableUseCaseImpl @Inject constructor(
    private val timetableRepo: TimetableRepo
) : RetrieveUserTimetableUseCase {
    override fun invoke(email: String) = timetableRepo.getTimetable(email)
}