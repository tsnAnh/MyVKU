package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.repositories.TimetableRepo
import javax.inject.Inject

interface RetrieveUserTimetableUseCase {
    fun invoke(email: String): List<Subject>
}

class RetrieveUserTimetableUseCaseImpl @Inject constructor(
    private val timetableRepo: TimetableRepo
) : RetrieveUserTimetableUseCase {
    override fun invoke(email: String) = timetableRepo.getTimetable(email)
}