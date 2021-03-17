package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.repositories.AbsenceRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveAbsencesUseCase {
    fun getAbsences(): Flow<State<List<Absence>>>
    suspend fun refresh()
}

class RetrieveAbsencesUseCaseImpl @Inject constructor(
    private val absenceRepo: AbsenceRepo
) : RetrieveAbsencesUseCase {
    override fun getAbsences() = absenceRepo.getAbsences()

    override suspend fun refresh() = absenceRepo.refresh()
}