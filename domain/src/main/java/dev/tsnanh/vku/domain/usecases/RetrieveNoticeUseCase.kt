package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.repositories.NoticeRepo
import javax.inject.Inject

interface RetrieveNoticeUseCase {
    fun absence(time: String): LiveData<Resource<List<Absence>>>
    fun makeUpClass(time: String): LiveData<Resource<List<MakeUpClass>>>
}

class RetrieveNoticeUseCaseImpl @Inject constructor(
    private val noticeRepo: NoticeRepo
) : RetrieveNoticeUseCase {
    override fun absence(time: String) = noticeRepo.getAbsenceNotice(time).asLiveData()

    override fun makeUpClass(time: String) = noticeRepo.getMakeUpClass(time).asLiveData()
}