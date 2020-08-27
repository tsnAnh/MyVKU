package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NoticeRepo {
    fun getAbsenceNotice(time: String): Flow<List<Absence>>
    fun getMakeUpClass(time: String): Flow<List<MakeUpClass>>
}

class NoticeRepoImpl @Inject constructor() : NoticeRepo {
    override fun getAbsenceNotice(time: String): Flow<List<Absence>> {
        return flow {
            emit(
                VKUServiceApi.network.getAbsenceNotice(time = time)
            )
        }
    }

    override fun getMakeUpClass(time: String): Flow<List<MakeUpClass>> {
        return flow {
            emit(
                VKUServiceApi.network.getMakeUpClassNotice(time = time)
            )
        }
    }
}