package dev.tsnanh.myvku.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.usecases.RetrieveUserTimetableUseCase
import dev.tsnanh.myvku.receivers.SchoolReminderReceiver
import dev.tsnanh.myvku.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

private const val MORNING_GROUP = "dev.tsnanh.myvku.schoolReminder.morning"
private const val AFTERNOON_GROUP = "dev.tsnanh.myvku.schoolReminder.afternoon"
private const val EVENING_GROUP = "dev.tsnanh.myvku.schoolReminder.evening"
private const val NIGHT_GROUP = "dev.tsnanh.myvku.schoolReminder.night"
private const val DAY_OFF_GROUP = "dev.tsnanh.myvku.schoolReminder.dayOff"
private const val ERROR_GROUP = "dev.tsnanh.myvku.schoolReminder.error"

@AndroidEntryPoint
class SchoolReminderService : Service() {
    private val notificationManager by lazy {
        getSystemService<NotificationManager>()
    }

    @Inject
    lateinit var retrieveTimetableUseCase: RetrieveUserTimetableUseCase
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("Service called")
        val builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification
                    .Builder(this, getString(R.string.school_reminder_channel_id))
                    .setTicker(getString(R.string.text_school_remider))
                    .setContentTitle(getString(R.string.text_my_vku_is_running))
                    .build()
            } else {
                NotificationCompat.Builder(this, getString(R.string.school_reminder_channel_id))
                    .setTicker(getString(R.string.text_school_remider))
                    .setContentTitle(getString(R.string.text_my_vku_is_running))
                    .build()
            }
        startForeground(1, builder)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        GlobalScope.launch {
            try {
                val context = this@SchoolReminderService
                val email = withContext(Dispatchers.IO) { intent?.getStringExtra("email") }

                // return part of the day, 0 -> 11 Morning, 12 -> 18 Afternoon, 19 -> 23 Night
                val partOfTheDay = when (Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 -> 0
                    12, 13, 14, 15, 16, 17 -> 1
                    18, 19, 20 -> 2
                    21, 22, 23 -> 3
                    else -> throw IllegalArgumentException("Wrong hour")
                }
                // [END]
                val result = emptyList<Subject>()
                // Get current day of week
                val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]

                // Filter current day only
                val list = result.filter { subject ->
                    dayOfWeekFilter(subject, dayOfWeek)
                }

                if (list.isEmpty()) {
                    // day off
                    if (partOfTheDay == 0) {
                        notificationManager?.sendSchoolReminderNotification(
                            Random.nextInt(),
                            context
                                .getString(R.string.title_notification_school_reminder_no_subject),
                            context
                                .getString(R.string.content_notification_school_reminder_no_subject),
                            null,
                            DAY_OFF_GROUP,
                            context
                        )
                    }
                } else {
                    when (partOfTheDay) {
                        // Morning
                        0 -> notifyMorningSubjects(list, false)
                        // Afternoon
                        1 -> notifyAfternoonSubjects(list)
                        // Evening
                        2 -> notificationManager?.sendSchoolReminderNotification(
                            Random.nextInt(),
                            getString(R.string.title_school_reminder_how_is_your_day),
                            getString(R.string.text_good_evening),
                            null,
                            EVENING_GROUP,
                            context
                        )
                        // Night
                        3 -> {
                            val tomorrowSubjects = result.filter {
                                dayOfWeekFilter(
                                    it,
                                    when (val tomorrow =
                                        Calendar.getInstance()[Calendar.DAY_OF_WEEK]) {
                                        7 -> Calendar.SUNDAY
                                        else -> tomorrow + 1
                                    }
                                )
                            }
                            notifyMorningSubjects(
                                tomorrowSubjects, true
                            )
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val newIntent =
                        Intent(
                            this@SchoolReminderService,
                            SchoolReminderReceiver::class.java
                        ).apply {
                            putExtra("email", email)
                        }
                    setAlarmApi23AndAbove(context, newIntent)
                }
//            wakeLock.release()
            } catch (e: IllegalArgumentException) {
                notificationManager?.sendSchoolReminderNotification(
                    Random(100).nextInt(),
                    getString(R.string.text_something_went_wrong),
                    getString(R.string.text_sorry_for_inconvenience),
                    null,
                    ERROR_GROUP,
                    applicationContext
                )
            }
        }
        stopForeground(true)
        stopSelf()
        return START_NOT_STICKY
    }

    private fun notifyMorningSubjects(list: List<Subject>, fromNight: Boolean) {
        val morningSubjects = list.filter { subject ->
            subject.week.trim()
                .isNotEmpty() && subject.lesson.trim()[0].toString()
                .matches("[1-5]".toRegex())
        }
        if (morningSubjects.isEmpty()) {
            notificationManager?.sendSchoolReminderNotification(
                Random.nextInt(),
                getString(R.string.title_notification_school_reminder_no_subject_morning),
                if (!fromNight) getString(R.string.message_notification_school_reminder_no_subject_morning)
                else getString(R.string.content_notification_school_reminder_no_subject_tomorrow_morning),
                null,
                if (!fromNight) MORNING_GROUP else NIGHT_GROUP,
                this
            )
        } else {
            morningSubjects.forEach { subject ->
                if (!subject.dayOfWeek.startsWith("_")) {
                    notificationManager?.sendSchoolReminderNotification(
                        Random.nextInt(),
                        subject.className,
                        getString(
                            if (!fromNight) R.string.message_notification_school_reminder_has_subject_morning else R.string.message_notification_school_reminder_has_subject_tomorrow,
                            subject.className,
                            subject.lesson.getExactHourStringFromLesson(),
                            subject.room
                        ),
                        subject.className,
                        if (!fromNight) MORNING_GROUP else NIGHT_GROUP,
                        this
                    )
                }
            }
        }
    }

    private fun notifyAfternoonSubjects(list: List<Subject>) {
        // Filter afternoon subject
        val afternoonSubjects = list.filter { subject ->
            subject.week.trim()
                .isNotEmpty() && subject.lesson.trim()[0].toString()
                .matches(Regex("[6-9]"))
        }
        if (afternoonSubjects.isEmpty()) {
            notificationManager?.sendSchoolReminderNotification(
                Random.nextInt(),
                getString(R.string.title_notification_school_reminder_no_subject_afternoon),
                getString(R.string.message_notification_school_reminder_no_subject_afternoon),
                null,
                AFTERNOON_GROUP,
                this
            )
        } else {
            afternoonSubjects.forEach { subject ->
                if (!subject.dayOfWeek.startsWith("_")) {
                    notificationManager?.sendSchoolReminderNotification(
                        Random.nextInt(),
                        subject.className,
                        getString(
                            R.string.message_notification_school_reminder_has_subject_afternoon,
                            subject.className,
                            subject.lesson.getExactHourStringFromLesson(),
                            subject.room
                        ),
                        subject.className,
                        AFTERNOON_GROUP,
                        this
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarmApi23AndAbove(
        context: Context,
        intent: Intent,
    ) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val timeSubtract =
            sharedPreferences.getString(getString(R.string.school_reminder_time_key), "30")!!
                .toInt() * 1000 * 60
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val morningIntent = PendingIntent.getBroadcast(
            context,
            RC_SCHOOL_REMINDER_MORNING, intent, PendingIntent.FLAG_UPDATE_CURRENT
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
        when (Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
            in 6..7 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    morningIntent
                )
            }
            in 12..13 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    afternoonIntent
                )
            }
            18 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarEvening.timeInMillis + AlarmManager.INTERVAL_DAY,
                    eveningIntent
                )
            }
            21 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarNight.timeInMillis + AlarmManager.INTERVAL_DAY,
                    nightIntent
                )
            }
            else -> {
                // Ignore
            }
        }
    }
}