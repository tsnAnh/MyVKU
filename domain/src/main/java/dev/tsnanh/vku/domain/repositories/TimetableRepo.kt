package dev.tsnanh.vku.domain.repositories

import androidx.lifecycle.LiveData
import dev.tsnanh.vku.domain.database.VKUDao
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface TimetableRepo {
    fun getTimetableLiveData(): LiveData<List<Subject>>
    suspend fun refreshSubjects(email: String)
    suspend fun getTimetable(email: String): Resource<List<Subject>>
    fun getTimetableWithFilter(type: Int): Flow<List<Subject>>
}

class TimetableRepoImpl @Inject constructor(
    private val dao: VKUDao
) : TimetableRepo {
    override fun getTimetableLiveData(): LiveData<List<Subject>> {
        return dao.getAllSubjectsLiveData()
    }

    override suspend fun refreshSubjects(email: String) {
        withContext(Dispatchers.IO) {
            val subjects =
                VKUServiceApi.network.getTimetable(email = email)
            dao.insertAllSubjects(*subjects.toTypedArray())
        }
    }

    override suspend fun getTimetable(email: String): Resource<List<Subject>> {
        return try {
            Resource.Success(dao.getAllSubjects())
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
        }
    }

    override fun getTimetableWithFilter(type: Int): Flow<List<Subject>> {
        val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
        return dao.getTimetableWithFilter(when (type) {
            0 -> ""
            1 -> dayOfWeek.toVietnameseDayOfWeek
            else -> (if (dayOfWeek == 7) 1 else dayOfWeek + 1).toVietnameseDayOfWeek
        })
    }
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