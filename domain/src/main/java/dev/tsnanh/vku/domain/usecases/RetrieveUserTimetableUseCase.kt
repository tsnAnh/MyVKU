package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.repositories.TimetableRepo
import javax.inject.Inject

interface RetrieveUserTimetableUseCase {
    suspend fun invoke(email: String): Resource<List<Subject>>
}

class RetrieveUserTimetableUseCaseImpl @Inject constructor(
    private val timetableRepo: TimetableRepo
) : RetrieveUserTimetableUseCase {
    override suspend fun invoke(email: String): Resource<List<Subject>> {
        return try {
            timetableRepo.getTimetable(email)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
        }
    }
}