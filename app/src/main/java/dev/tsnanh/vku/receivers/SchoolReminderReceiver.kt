package dev.tsnanh.vku.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dev.tsnanh.vku.services.SchoolReminderService

class SchoolReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val newIntent = Intent(context, SchoolReminderService::class.java).apply {
            putExtra("email", intent.getStringExtra("email"))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(newIntent)
        } else {
            context.startService(newIntent)
        }
        // Kotlin Coroutines global scope
//        GlobalScope.launch {
//            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
//            val wakeLock =
//                powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.WAKE_LOCK_TAG)
//            wakeLock.acquire(10 * 60 * 1000L) // 10 minutes
//
//            // [START] Prepare data
//            Timber.d("Receiver called")
//            val retrieveTimetableUseCase by inject(RetrieveUserTimetableUseCase::class.java)
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val email = intent.getStringExtra("email")
//
//            // return part of the day, 0 -> 11 Morning, 12 -> 18 Afternoon, 19 -> 23 Night
//            val partOfTheDay = when (Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
//                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 -> 0
//                12, 13, 14, 15, 16, 17 -> 1
//                18, 19, 20 -> 2
//                21, 22, 23 -> 3
//                else -> throw IllegalArgumentException("Wrong hour")
//            }
//            // [END]
//            when (val result =
//                withContext(Dispatchers.IO) {
//                    retrieveTimetableUseCase.invoke(
//                        email!!
//                    )
//                }) {
//                is Resource.Error -> Timber.d(result.message)
//                is Resource.Success -> {
//                    Timber.d("success")
//                    // Get current day of week
//                    val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
//
//                    // Filter current day only
//                    val list = result.data!!.filter { subject ->
//                        dayOfWeekFilter(subject, dayOfWeek)
//                    }
//
//                    if (list.isEmpty()) {
//                        // day off
//                        if (partOfTheDay == 0) {
//                            notificationManager.sendSchoolReminderNotification(
//                                Random(100).nextInt(),
//                                context
//                                    .getString(R.string.title_notification_school_reminder_no_subject),
//                                context
//                                    .getString(R.string.content_notification_school_reminder_no_subject),
//                                null,
//                                context
//                            )
//                        }
//                    } else {
//                        when (partOfTheDay) {
//                            // Morning
//                            0 -> {
//                                // Filter morning subjects
//                                val morningSubjects = list.filter { subject ->
//                                    subject.week.trim()
//                                        .isNotEmpty() && subject.lesson.trim()[0].toString()
//                                        .matches(Regex("[1-5]"))
//                                }
//                                if (morningSubjects.isEmpty()) {
//                                    notificationManager.sendSchoolReminderNotification(
//                                        Random(100).nextInt(),
//                                        context
//                                            .getString(R.string.title_notification_school_reminder_no_subject_morning),
//                                        context
//                                            .getString(R.string.content_notification_school_reminder_no_subject_morning),
//                                        null,
//                                        context
//                                    )
//                                } else {
//                                    morningSubjects.forEach { subject ->
//                                        notificationManager.sendSchoolReminderNotification(
//                                            subject.dayOfWeek.getHourFromStringLesson(),
//                                            subject.className,
//                                            "You have ${subject.className} at " +
//                                                "${subject.lesson.getHourFromStringLesson()} at " +
//                                                "${subject.room} in this morning!",
//                                            subject.className,
//                                            context
//                                        )
//                                    }
//                                }
//                            }
//                            // Afternoon
//                            1 -> {
//                                val afternoonSubjects = list.filter { subject ->
//                                    subject.week.trim()
//                                        .isNotEmpty() && subject.lesson.trim()[0].toString()
//                                        .matches(Regex("[6-9]"))
//                                }
//                                if (afternoonSubjects.isEmpty()) {
//                                    notificationManager.sendSchoolReminderNotification(
//                                        Random(100).nextInt(),
//                                        context
//                                            .getString(R.string.title_notification_school_reminder_no_subject_afternoon),
//                                        context
//                                            .getString(R.string.content_notification_school_reminder_no_subject_afternoon),
//                                        null,
//                                        context
//                                    )
//                                } else {
//                                    afternoonSubjects.forEach { subject ->
//                                        notificationManager.sendSchoolReminderNotification(
//                                            subject.lesson.getHourFromStringLesson(),
//                                            subject.className,
//                                            "You have ${subject.className} at " +
//                                                    "${subject.lesson.getHourFromStringLesson()} at " +
//                                                    "${subject.room} in this afternoon!",
//                                            subject.className,
//                                            context
//                                        )
//                                    }
//                                }
//                            }
//                            2 -> {
//                                notificationManager.sendSchoolReminderNotification(
//                                    Random(10).nextInt(),
//                                    "How is your day?",
//                                    "Good evening here!",
//                                    null,
//                                    context
//                                )
//                            }
//                            3 -> {
//                                // TODO: create notification good night user
//                                notificationManager.sendSchoolReminderNotification(
//                                    Random(100).nextInt(),
//                                    "Good night",
//                                    "Good night here!",
//                                    null,
//                                    context
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//            if (Build.VERSION.SDK_INT >= M) {
//                val newIntent = Intent(context, SchoolReminderReceiver::class.java).apply {
//                    putExtra("email", email)
//                }
//                setAlarmApi23AndAbove(context, newIntent)
//            }
//            wakeLock.release()
//        }
    }

//    @RequiresApi(M)
//    private fun setAlarmApi23AndAbove(
//        context: Context,
//        intent: Intent
//    ) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val morningIntent = PendingIntent.getBroadcast(
//            context,
//            RC_SCHOOL_REMINDER_MORNING,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val afternoonIntent = PendingIntent.getBroadcast(
//            context,
//            RC_SCHOOL_REMINDER_AFTERNOON,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val eveningIntent = PendingIntent.getBroadcast(
//            context,
//            RC_SCHOOL_REMINDER_EVENING,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val nightIntent = PendingIntent.getBroadcast(
//            context,
//            RC_SCHOOL_REMINDER_NIGHT,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        when (Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
//            in 0..6 -> {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    morningIntent
//                )
//            }
//            in 6..12 -> {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    afternoonIntent
//                )
//            }
//            in 12..18 -> {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarEvening.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    eveningIntent
//                )
//            }
//            in 18..21 -> {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarNight.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    nightIntent
//                )
//            }
//            else -> {
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    morningIntent
//                )
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    afternoonIntent
//                )
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarEvening.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    eveningIntent
//                )
//                alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    calendarNight.timeInMillis + AlarmManager.INTERVAL_DAY,
//                    nightIntent
//                )
//            }
//        }
//    }
}
