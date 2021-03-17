package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import java.util.Calendar
import javax.inject.Inject

interface TimetableRepo {
    fun getLocalTimetable(email: String, type: TimetableFilter): Flow<State<List<Subject>>>
    suspend fun refresh(email: String)
}

class TimetableRepoImpl @Inject constructor(
    private val vkuDao: VKUDao
) : TimetableRepo {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLocalTimetable(email: String, type: TimetableFilter) = flow {
        emit(State.loading())
        emitAll(vkuDao.getAllSubjectsFlow().mapLatest {
            State.success(it.filter { subject ->
                val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
                when (type) {
                    TimetableFilter.ALL -> true
                    TimetableFilter.TODAY -> dayOfWeek.toVietnameseDayOfWeek == subject.dayOfWeek
                    TimetableFilter.TOMORROW -> (if (dayOfWeek == 7) 1 else dayOfWeek + 1)
                        .toVietnameseDayOfWeek == subject.dayOfWeek
                }
            })
        })
    }.catch { t ->
        emit(State.error(t))
    }.flowOn(Dispatchers.IO)

    override suspend fun refresh(email: String) {
        val subjects = VKUServiceApi.network.getTimetable(email = email).sortedBy { it.className }
        val localSubjects = vkuDao.getAllSubjects().sortedBy { it.className }
        if (localSubjects.isEmpty() || (!subjects.containsAll(localSubjects) && !localSubjects.containsAll(
                subjects
            ))
        ) {
            vkuDao.deleteAllSubjects()
            vkuDao.insertAllSubjects(*subjects.toTypedArray())
        }
    }
}

enum class TimetableFilter {
    ALL, TODAY, TOMORROW
}

// Day of week (Vietnamese)
private const val MONDAY = "Hai"
private const val TUESDAY = "Ba"
private const val WEDNESDAY = "Tư"
private const val THURSDAY = "Năm"
private const val FRIDAY = "Sáu"
private const val SATURDAY = "Bảy"

private val Int.toVietnameseDayOfWeek: String
    get() = when (this) {
        Calendar.SUNDAY -> ""
        Calendar.MONDAY -> MONDAY
        Calendar.TUESDAY -> TUESDAY
        Calendar.WEDNESDAY -> WEDNESDAY
        Calendar.THURSDAY -> THURSDAY
        Calendar.FRIDAY -> FRIDAY
        Calendar.SATURDAY -> SATURDAY
        else -> throw IllegalArgumentException("Wrong day of week value")
    }