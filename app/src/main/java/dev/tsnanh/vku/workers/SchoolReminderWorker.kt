package dev.tsnanh.vku.workers

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableUseCase
import dev.tsnanh.vku.utils.sendSchoolReminderNotification
import kotlinx.coroutines.coroutineScope
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class SchoolReminderWorker(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork() = coroutineScope {
        // [START] Prepare data
        val retrieveTimetableUseCase by inject(RetrieveUserTimetableUseCase::class.java)
        val email = inputData.getString("email")
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // [END]
        when (val result =
            retrieveTimetableUseCase.invoke("http://daotao.sict.udn.vn/tkb", email!!)) {
            is Resource.Success -> {
                val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
                val list = result.data!!.filter { subject ->
                    when (subject.dayOfWeek) {
                        "Hai" -> Calendar.MONDAY == dayOfWeek
                        "Ba" -> Calendar.TUESDAY == dayOfWeek
                        "Tư" -> Calendar.WEDNESDAY == dayOfWeek
                        "Năm" -> Calendar.THURSDAY == dayOfWeek
                        "Sáu" -> Calendar.FRIDAY == dayOfWeek
                        "Bảy" -> Calendar.SATURDAY == dayOfWeek
                        else -> Calendar.SUNDAY == dayOfWeek
                    }
                }
                if (list.isEmpty()) {
                    notificationManager.sendSchoolReminderNotification(
                        applicationContext
                            .getString(R.string.title_notification_school_reminder_no_subject),
                        applicationContext
                            .getString(R.string.content_notification_school_reminder_no_subject),
                        applicationContext
                    )
                }
            }
        }

        Result.success()
    }
}