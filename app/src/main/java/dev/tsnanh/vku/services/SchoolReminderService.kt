package dev.tsnanh.vku.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableUseCase
import dev.tsnanh.vku.receivers.SchoolReminderReceiver
import dev.tsnanh.vku.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

private const val MORNING = 0
private const val AFTERNOON = 1
private const val EVENING = 2
private const val NIGHT = 3
private const val DAY_OFF = 9999

@AndroidEntryPoint
class SchoolReminderService : Service() {
    private val notificationManager by lazy {
        getSystemService<NotificationManager>()
    }
    @Inject
    lateinit var retrieveTimetableUseCase: RetrieveUserTimetableUseCase
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification
                    .Builder(this, getString(R.string.school_reminder_channel_id))
                    .setTicker("School reminder")
                    .setContentTitle("My VKU is running...")
                    .build()
            } else {
                NotificationCompat.Builder(this, getString(R.string.school_reminder_channel_id))
                    .setTicker("School reminder")
                    .setContentTitle("My VKU is running...")
                    .build()
            }
        startForeground(88, builder)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val context = this@SchoolReminderService
                val email = intent?.getStringExtra("email")

                // return part of the day, 0 -> 11 Morning, 12 -> 18 Afternoon, 19 -> 23 Night
                val partOfTheDay = when (Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 -> 0
                    12, 13, 14, 15, 16, 17 -> 1
                    18, 19, 20 -> 2
                    21, 22, 23 -> 3
                    else -> throw IllegalArgumentException("Wrong hour")
                }
                // [END]
                when (val result =
                    withContext(Dispatchers.IO) {
                        email?.let {
                            retrieveTimetableUseCase.invoke(
                                it
                            )
                        }
                    }) {
                    is Resource.Error -> Timber.d(result.message)
                    is Resource.Success -> {
                        Timber.d("successfully load timetable")
                        // Get current day of week
                        val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]

                        // Filter current day only
                        val list = result.data!!.filter { subject ->
                            dayOfWeekFilter(subject, dayOfWeek)
                        }

                        if (list.isEmpty()) {
                            // day off
                            if (partOfTheDay == 0) {
                                notificationManager?.sendSchoolReminderNotification(
                                    DAY_OFF,
                                    context
                                        .getString(R.string.title_notification_school_reminder_no_subject),
                                    context
                                        .getString(R.string.content_notification_school_reminder_no_subject),
                                    null,
                                    context
                                )
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
                                        notificationManager?.sendSchoolReminderNotification(
                                            MORNING,
                                            context
                                                .getString(R.string.title_notification_school_reminder_no_subject_morning),
                                            context
                                                .getString(R.string.content_notification_school_reminder_no_subject_morning),
                                            null,
                                            context
                                        )
                                    } else {
                                        morningSubjects.forEach { subject ->
                                            if (!subject.dayOfWeek.startsWith("_")) {
                                                notificationManager?.sendSchoolReminderNotification(
                                                    MORNING,
                                                    subject.className,
                                                    "You have ${subject.className} at " +
                                                            "${subject.lesson.getHourFromStringLesson()} at " +
                                                            "${subject.room} in this morning!",
                                                    subject.className,
                                                    context
                                                )
                                            }
                                        }
                                    }
                                }
                                // Afternoon
                                1 -> {
                                    // Filter afternoon subject
                                    val afternoonSubjects = list.filter { subject ->
                                        subject.week.trim()
                                            .isNotEmpty() && subject.lesson.trim()[0].toString()
                                            .matches(Regex("[6-9]"))
                                    }
                                    if (afternoonSubjects.isEmpty()) {
                                        notificationManager?.sendSchoolReminderNotification(
                                            AFTERNOON,
                                            context
                                                .getString(R.string.title_notification_school_reminder_no_subject_afternoon),
                                            context
                                                .getString(R.string.content_notification_school_reminder_no_subject_afternoon),
                                            null,
                                            context
                                        )
                                    } else {
                                        afternoonSubjects.forEach { subject ->
                                            if (!subject.dayOfWeek.startsWith("_")) {
                                                notificationManager?.sendSchoolReminderNotification(
                                                    AFTERNOON,
                                                    subject.className,
                                                    "You have ${subject.className} at " +
                                                            "${subject.lesson.getHourFromStringLesson()} at " +
                                                            "${subject.room} in this afternoon!",
                                                    subject.className,
                                                    context
                                                )
                                            }
                                        }
                                    }
                                }
                                2 -> {
                                    notificationManager?.sendSchoolReminderNotification(
                                        EVENING,
                                        "How is your day?",
                                        "Good evening here!",
                                        null,
                                        context
                                    )
                                }
                                3 -> {
                                    // TODO: create notification good night user
                                    notificationManager?.sendSchoolReminderNotification(
                                        NIGHT,
                                        "Good night",
                                        "Good night here!",
                                        null,
                                        context
                                    )
                                }
                            }
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val newIntent =
                        Intent(this@SchoolReminderService,
                            SchoolReminderReceiver::class.java).apply {
                            putExtra("email", email)
                        }
                    setAlarmApi23AndAbove(context, newIntent)
                }
//            wakeLock.release()
            } catch (e: IllegalArgumentException) {
                notificationManager?.sendSchoolReminderNotification(
                    Random(100).nextInt(),
                    "Something went wrong!",
                    "We are sorry for unconvenient.",
                    null,
                    applicationContext
                )
            }
        }
        stopForeground(true)
        stopSelf()
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarmApi23AndAbove(
        context: Context,
        intent: Intent
    ) {
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
            6 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY,
                    morningIntent
                )
            }
            12 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY,
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
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("Destroyed! ")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Timber.d("Removed")
    }
}