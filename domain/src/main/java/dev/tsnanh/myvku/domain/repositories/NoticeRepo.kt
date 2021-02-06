package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.domain.entities.MakeUpClass
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface NoticeRepo {
    fun getAbsenceNotice(time: String): Flow<State<List<Absence>>>
    fun getMakeUpClass(time: String): Flow<State<List<MakeUpClass>>>
}

class NoticeRepoImpl @Inject constructor() : NoticeRepo {
    override fun getAbsenceNotice(time: String): Flow<State<List<Absence>>> {
        return flow {
            emit(State.Loading())
            emit(
                State.Success(VKUServiceApi.network.getAbsenceNotice(time = time))
            )
        }.catch {
            emit(State.error(it))
        }.flowOn(Dispatchers.IO)
    }

    override fun getMakeUpClass(time: String): Flow<State<List<MakeUpClass>>> {
        return flow {
            emit(State.Loading())
            emit(
                State.Success(VKUServiceApi.network.getMakeUpClassNotice(time = time))
            )
        }.catch {
            emit(State.error(it))
        }.flowOn(Dispatchers.IO)
    }
}