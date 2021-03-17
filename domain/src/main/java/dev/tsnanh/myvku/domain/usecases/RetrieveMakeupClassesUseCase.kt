package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.MakeupClass
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.repositories.MakeupRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveMakeupClassesUseCase {
    fun getMakeupClasses(): Flow<State<List<MakeupClass>>>
    suspend fun refresh()
}

class RetrieveMakeupClassesUseCaseImpl @Inject constructor(
    private val makeupRepo: MakeupRepo
) : RetrieveMakeupClassesUseCase {
    override fun getMakeupClasses() = makeupRepo.getMakeupClasses()

    override suspend fun refresh() = makeupRepo.refresh()
}