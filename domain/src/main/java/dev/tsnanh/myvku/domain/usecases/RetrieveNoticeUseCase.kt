package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.domain.entities.MakeUpClass
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.repositories.NoticeRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveNoticeUseCase {
    fun absence(time: String): Flow<State<List<Absence>>>
    fun makeUpClass(time: String): Flow<State<List<MakeUpClass>>>
}

class RetrieveNoticeUseCaseImpl @Inject constructor(
    private val noticeRepo: NoticeRepo
) : RetrieveNoticeUseCase {
    override fun absence(time: String) = noticeRepo.getAbsenceNotice(time)

    override fun makeUpClass(time: String) = noticeRepo.getMakeUpClass(time)
}