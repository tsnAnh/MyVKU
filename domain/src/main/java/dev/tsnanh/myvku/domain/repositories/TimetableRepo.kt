package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar
import javax.inject.Inject

interface TimetableRepo {
    fun getTimetable(email: String, type: TimetableFilter): Flow<State<List<Subject>>>
}

class TimetableRepoImpl @Inject constructor() : TimetableRepo {
    override fun getTimetable(email: String, type: TimetableFilter) = flow {
        emit(State.loading())
        val subjects = VKUServiceApi.network.getTimetable(email = email)
            .filter {
                val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
                when (type) {
                    TimetableFilter.ALL -> true
                    TimetableFilter.TODAY -> dayOfWeek.toVietnameseDayOfWeek == it.dayOfWeek
                    TimetableFilter.TOMORROW -> (if (dayOfWeek == 7) 0 else dayOfWeek + 1)
                        .toVietnameseDayOfWeek == it.dayOfWeek
                }
            }
        emit(State.success(subjects))
    }.catch { t ->
        emit(State.error(t))
    }.flowOn(Dispatchers.IO)
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