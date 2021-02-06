package dev.tsnanh.myvku.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.preference.PreferenceManager
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.receivers.SchoolReminderReceiver
import timber.log.Timber
import java.util.*

const val RC_SCHOOL_REMINDER_MORNING = 1000
const val RC_SCHOOL_REMINDER_AFTERNOON = 2000
const val RC_SCHOOL_REMINDER_EVENING = 3000
const val RC_SCHOOL_REMINDER_NIGHT = 4000
val calendarMorning = prepareCalendar(7, 30)
val calendarAfternoon = prepareCalendar(13, 0)
val calendarEvening = prepareCalendar(18, 0)
val calendarNight = prepareCalendar(21, 0)

fun Context.setSchoolReminderAlarm(email: String) {
    Timber.i("Created")
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    val timeSubtract =
        sharedPreferences.getString(getString(R.string.school_reminder_time_key), "30")!!
            .toInt() * 1000 * 60

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(this, SchoolReminderReceiver::class.java).apply {
        putExtra("email", email)
    }

    val morningIntent = PendingIntent.getBroadcast(
        this,
        RC_SCHOOL_REMINDER_MORNING,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val afternoonIntent = PendingIntent.getBroadcast(
        this,
        RC_SCHOOL_REMINDER_AFTERNOON,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val eveningIntent = PendingIntent.getBroadcast(
        this,
        RC_SCHOOL_REMINDER_EVENING,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val nightIntent = PendingIntent.getBroadcast(
        this,
        RC_SCHOOL_REMINDER_NIGHT,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendarMorning.timeInMillis - timeSubtract,
            AlarmManager.INTERVAL_DAY,
            morningIntent
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendarAfternoon.timeInMillis - timeSubtract,
            AlarmManager.INTERVAL_DAY,
            afternoonIntent
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendarEvening.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            eveningIntent
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendarNight.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            nightIntent
        )
    } else {
        when (Calendar.getInstance()[Calendar.HOUR_OF_DAY]) {
            in 0..7 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarMorning.timeInMillis - timeSubtract,
                    morningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarAfternoon.timeInMillis - timeSubtract,
                    afternoonIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarEvening.timeInMillis,
                    eveningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarNight.timeInMillis,
                    nightIntent
                )
            }
            in 8..13 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    morningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarAfternoon.timeInMillis - timeSubtract,
                    afternoonIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarEvening.timeInMillis,
                    eveningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarNight.timeInMillis,
                    nightIntent
                )
            }
            in 14..18 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    morningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    afternoonIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarEvening.timeInMillis,
                    eveningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarNight.timeInMillis,
                    nightIntent
                )
            }
            in 18..21 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    morningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    afternoonIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarEvening.timeInMillis + AlarmManager.INTERVAL_DAY,
                    eveningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarNight.timeInMillis,
                    nightIntent
                )
            }
            in 22..23 -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarMorning.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
                    morningIntent
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarAfternoon.timeInMillis + AlarmManager.INTERVAL_DAY - timeSubtract,
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
    Timber.d("Create successfully!")
}

fun prepareCalendar(hourOfDay: Int, minute: Int): Calendar {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hourOfDay)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

@Throws(IllegalArgumentException::class)
fun String.getHourFromLesson(): Int {
    return when (this[0]) {
        '1', '2' -> 7
        '3', '4' -> 9
        '5' -> 10
        '6' -> 12
        '7' -> 13
        '8' -> 14
        '9' -> 15
        // add more later
        else -> throw IllegalArgumentException("Wrong lesson string")
    }
}

fun String.getExactHourStringFromLesson(): String = when (this[0]) {
    '1', '2' -> "7h30"
    '3', '4' -> "9h30"
    '5' -> "11h30"
    '6', '7' -> "13h00"
    '8', '9' -> "15h00"
    // add more later
    else -> throw IllegalArgumentException("Wrong lesson string")
}

fun String.getMinutesFromStringLesson(): Int {
    return when (this[0]) {
        '1', '3' -> 0
        '2', '4' -> 50
        '5' -> 40
        '6', '8' -> 30
        '7', '9' -> 20
        // add more later
        else -> throw IllegalArgumentException("Wrong lesson string")
    }
}

@Throws(IllegalArgumentException::class)
fun String.getDayOfWeekFromString(): Int {
    return when (this) {
        Constants.MONDAY -> Calendar.MONDAY
        Constants.TUESDAY -> Calendar.TUESDAY
        Constants.WEDNESDAY -> Calendar.WEDNESDAY
        Constants.THURSDAY -> Calendar.THURSDAY
        Constants.FRIDAY -> Calendar.FRIDAY
        Constants.SATURDAY -> Calendar.SATURDAY
        else -> throw IllegalArgumentException("Wrong day of week string")
    }
}

fun dayOfWeekFilter(subject: Subject, dayOfWeek: Int): Boolean {
    return when (subject.dayOfWeek) {
        Constants.MONDAY -> Calendar.MONDAY == dayOfWeek
        Constants.TUESDAY -> Calendar.TUESDAY == dayOfWeek
        Constants.WEDNESDAY -> Calendar.WEDNESDAY == dayOfWeek
        Constants.THURSDAY -> Calendar.THURSDAY == dayOfWeek
        Constants.FRIDAY -> Calendar.FRIDAY == dayOfWeek
        Constants.SATURDAY -> Calendar.SATURDAY == dayOfWeek
        else -> Calendar.SUNDAY == dayOfWeek
    }
}