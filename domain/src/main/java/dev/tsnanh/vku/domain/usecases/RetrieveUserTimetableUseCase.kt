package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.repositories.TimetableRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveUserTimetableUseCase {
    suspend fun invoke(url: String, email: String): Resource<List<Subject>>
}

class RetrieveUserTimetableUseCaseImpl : RetrieveUserTimetableUseCase {
    private val timetableRepo by inject(TimetableRepo::class.java)
    override suspend fun invoke(url: String, email: String): Resource<List<Subject>> {
        return try {
            timetableRepo.getTimetable(url, email)
        } catch (e: Throwable) {
            ErrorHandler.handleError(e)
        }
    }
}