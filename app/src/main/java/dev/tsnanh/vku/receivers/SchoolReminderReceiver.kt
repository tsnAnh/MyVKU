package dev.tsnanh.vku.receivers

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableUseCase
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.RC_SCHOOL_REMINDER_AFTERNOON
import dev.tsnanh.vku.utils.RC_SCHOOL_REMINDER_EVENING
import dev.tsnanh.vku.utils.RC_SCHOOL_REMINDER_MORNING
import dev.tsnanh.vku.utils.RC_SCHOOL_REMINDER_NIGHT
import dev.tsnanh.vku.utils.calendarAfternoon
import dev.tsnanh.vku.utils.calendarEvening
import dev.tsnanh.vku.utils.calendarMorning
import dev.tsnanh.vku.utils.calendarNight
import dev.tsnanh.vku.utils.dayOfWeekFilter
import dev.tsnanh.vku.utils.getHourFromStringLesson
import dev.tsnanh.vku.utils.sendSchoolReminderNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import java.util.Calendar
import kotlin.random.Random

class SchoolReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences by inject(SharedPreferences::class.java)
        GlobalScope.launch {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock =
                powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.WAKE_LOCK_TAG)
            wakeLock.acquire(10 * 60 * 1000L) // 10 minutes

            // [START] Prepare data
            Timber.d("Receiver called")
            val retrieveTimetableUseCase by inject(RetrieveUserTimetableUseCase::class.java)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val email = intent.getStringExtra("email")

            // 0 -> 11 Morning, 12 -> 18 Afternoon, 19 -> 23 Night
            val partOfTheDay = when (Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 -> 0
                12, 13, 14, 15, 16, 17, 18 -> 1
                19, 20, 21, 22, 23 -> 2
                else -> throw IllegalArgumentException("Wrong hour")
            }
            // [END]
            when (val result =
                withContext(Dispatchers.IO) {
                    retrieveTimetableUseCase.invoke(
                        "http://daotao.sict.udn.vn/tkb",
                        email!!
                    )
                }) {
                is Resource.Error -> Timber.d(result.message)
                is Resource.Success -> {
                    // Get current day of week
                    val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]

                    // Filter this day only
                    val list = result.data!!.filter { subject ->
                        dayOfWeekFilter(subject, dayOfWeek)
                    }

                    if (list.isEmpty()) {
                        notificationManager.sendSchoolReminderNotification(
                            Random(100).nextInt(),
                            context
                                .getString(R.string.title_notification_school_reminder_no_subject),
                            context
                                .getString(R.string.content_notification_school_reminder_no_subject),
                            context
                        )
                        sharedPreferences.edit {
                            putBoolean("dayOff", true)
                        }
                    } else {
                        when (partOfTheDay) {
                            // Morning
                            0 -> {
                                // Filter morning subjects
                                val morningSubjects = list.filter { subject ->
                                    subject.week.trim()
                                        .isNotEmpty() && subject.lesson.trim()[0].toString()
                                        .matches(Regex("[1-5]"))
                                }
                                if (morningSubjects.isEmpty()) {
                                    notificationManager.sendSchoolReminderNotification(
                                        Random(100).nextInt(),
                                        context
                                            .getString(R.string.title_notification_school_reminder_no_subject_morning),
                                        context
                                            .getString(R.string.content_notification_school_reminder_no_subject_morning),
                                        context
                                    )
                                } else {
                                    morningSubjects.forEach { subject ->
                                        notificationManager.sendSchoolReminderNotification(
                                            subject.dayOfWeek.getHourFromStringLesson(),
                                            subject.title,
                                            "You have ${subject.className} at " +
                                                "${subject.lesson.getHourFromStringLesson()} at " +
                                                "${subject.room} in this morning!",
                                            context
                                        )
                                    }
                                }
                            }
                            // Afternoon
                            1 -> {
                                if (sharedPreferences.getBoolean("dayOff", false)) {
                                    return@launch
                                }
                                val afternoonSubjects = list.filter { subject ->
                                    subject.week.trim()
                                        .isNotEmpty() && subject.lesson.trim()[0].toString()
                                        .matches(Regex("[6-9]"))
                                }
                                if (afternoonSubjects.isEmpty()) {
                                    notificationManager.sendSchoolReminderNotification(
                                        Random(100).nextInt(),
                                        context
                                            .getString(R.string.title_notification_school_reminder_no_subject_afternoon),
                                        context
                                            .getString(R.string.content_notification_school_reminder_no_subject_afternoon),
                                        context
                                    )
                                } else {
                                    afternoonSubjects.forEach { subject ->
                                        notificationManager.sendSchoolReminderNotification(
                                            subject.lesson.getHourFromStringLesson(),
                                            subject.title,
                                            "You have ${subject.className} at " +
                                                "${subject.lesson.getHourFromStringLesson()} at " +
                                                "${subject.room} in this afternoon!",
                                            context
                                        )
                                    }
                                }
                            }
                            2 -> {
                                // TODO: create notification good night user
                                sharedPreferences.edit {
                                    putBoolean("dayOff", false)
                                }
                            }
                        }
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= M) {
                setAlarmApi23AndAbove(context, intent)
            }
            wakeLock.release()
        }
    }

    @RequiresApi(M)
    private fun setAlarmApi23AndAbove(
        context: Context,
        intent: Intent
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val morningIntent = PendingIntent.getBroadcast(
            context,
            RC_SCHOOL_REMINDER_MORNING,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val afternoonIntent = PendingIntent.getBroadcast(
            context,
            RC_SCHOOL_REMINDER_AFTERNOON,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val eveningIntent = PendingIntent.getBroadcast(
            context,
            RC_SCHOOL_REMINDER_EVENING,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val nightIntent = PendingIntent.getBroadcast(
            context,
            RC_SCHOOL_REMINDER_NIGHT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY,
            morningIntent
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY,
            afternoonIntent
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendarEvening.timeInMillis + AlarmManager.INTERVAL_DAY,
            eveningIntent
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendarNight.timeInMillis + AlarmManager.INTERVAL_DAY,
            nightIntent
        )
    }
}
