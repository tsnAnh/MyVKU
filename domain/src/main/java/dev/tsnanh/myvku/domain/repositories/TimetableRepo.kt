package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.database.VKUDao
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Calendar
import javax.inject.Inject

interface TimetableRepo {
    fun getTimetable(email: String): Flow<State<List<Subject>>>
}

class TimetableRepoImpl @Inject constructor(
    private val dao: VKUDao,
) : TimetableRepo {
    override fun getTimetable(email: String) = flow {
        emit(State.loading())
        val subjects = VKUServiceApi.network.getTimetable(email = email)
        emit(State.success(subjects))
    }.catch { t ->
        println(t.localizedMessage)
        when (t) {
            is ConnectException, is SocketTimeoutException, is SocketException, is UnknownHostException -> emit(
                State.error(t, dao.getAllSubjects().first()))
            else -> emit(State.error(t))
        }
    }.flowOn(Dispatchers.IO)
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