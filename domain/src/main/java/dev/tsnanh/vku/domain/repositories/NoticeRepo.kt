package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface NoticeRepo {
    fun getAbsenceNotice(time: String): Flow<Resource<List<Absence>>>
    fun getMakeUpClass(time: String): Flow<Resource<List<MakeUpClass>>>
}

class NoticeRepoImpl @Inject constructor() : NoticeRepo {
    override fun getAbsenceNotice(time: String): Flow<Resource<List<Absence>>> {
        return flow {
            try {
                emit(
                    Resource.Success(VKUServiceApi.network.getAbsenceNotice(time = time))
                )
            } catch (e: Exception) {
                emit(dev.tsnanh.vku.domain.handler.ErrorHandler.handleError<List<Absence>>(e))
            }
        }
    }

    override fun getMakeUpClass(time: String): Flow<Resource<List<MakeUpClass>>> {
        return flow {
            try {
                emit(
                    Resource.Success(VKUServiceApi.network.getMakeUpClassNotice(time = time))
                )
            } catch (e: Exception) {
                emit(dev.tsnanh.vku.domain.handler.ErrorHandler.Companion.handleError<List<MakeUpClass>>(
                    e))
            }
        }
    }
}