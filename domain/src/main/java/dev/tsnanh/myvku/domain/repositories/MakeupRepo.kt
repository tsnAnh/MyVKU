package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.entities.MakeupClass
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

interface MakeupRepo {
    fun getMakeupClasses(): Flow<State<List<MakeupClass>>>
    suspend fun refresh()
}

class MakeupRepoImpl @Inject constructor(
    private val dao: VKUDao
) : MakeupRepo {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMakeupClasses() = flow {
        emit(State.loading())
        emitAll(dao.getAllMakeupClassesFlow().mapLatest { State.success(it) })
    }.catch {
        emit(State.error(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun refresh() {
        val makeupClasses = VKUServiceApi.network.getMakeUpClassNotice()
        val subList = makeupClasses.sortedByDescending { it.dateMakeUp }.subList(0, 10)
        val localMakeupClasses = dao.getAllMakeupClassesWithLimit()
        if (localMakeupClasses.isEmpty() || (!subList.containsAll(localMakeupClasses) && !localMakeupClasses.containsAll(
                subList
            ))
        ) {
            dao.insertAllMakeupClasses(*makeupClasses.toTypedArray())
        }
    }
}