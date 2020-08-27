package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.repositories.NoticeRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveNoticeUseCase {
    fun absence(time: String): Flow<List<Absence>>
    fun makeUpClass(time: String): Flow<List<MakeUpClass>>
}

class RetrieveNoticeUseCaseImpl @Inject constructor(
    private val noticeRepo: NoticeRepo
) : RetrieveNoticeUseCase {
    override fun absence(time: String): Flow<List<Absence>> = noticeRepo.getAbsenceNotice(time)

    override fun makeUpClass(time: String): Flow<List<MakeUpClass>> =
        noticeRepo.getMakeUpClass(time)
}