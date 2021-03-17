package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

interface AbsenceRepo {
    fun getAbsences(): Flow<State<List<Absence>>>
    suspend fun refresh()
}

class AbsenceRepoImpl @Inject constructor(
    private val dao:  VKUDao
) : AbsenceRepo {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAbsences() = flow {
        emit(State.Loading())
        emitAll(dao.getAllAbsencesFlow().mapLatest { State.success(it) })
    }.catch {
        emit(State.error(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun refresh() {
        val absences = VKUServiceApi.network.getAbsenceNotice()
        val subList = absences.sortedByDescending { it.dateNotice }.subList(0, 10)
        val localAbsences = dao.getAllAbsencesWithLimit()
        if (localAbsences.isEmpty() || (!subList.containsAll(localAbsences) && !localAbsences.containsAll(subList))) {
            dao.insertAllAbsences(*absences.toTypedArray())
        }
    }
}