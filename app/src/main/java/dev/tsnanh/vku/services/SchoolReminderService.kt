package dev.tsnanh.vku.services
//
//import android.app.AlarmManager
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.os.IBinder
//import dev.tsnanh.vku.R
//import dev.tsnanh.vku.domain.entities.Resource
//import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableUseCase
//import dev.tsnanh.vku.utils.sendSchoolReminderNotification
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.koin.java.KoinJavaComponent
//import timber.log.Timber
//import java.util.*
//
//class SchoolReminderService : Service() {
//
//    override fun onBind(intent: Intent): IBinder? = null
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Timber.d("Destroyed dcm")
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        GlobalScope.launch(Dispatchers.Default) {
//            // [START] Prepare data
//            Timber.d("Receiver called")
//            val retrieveTimetableUseCase by KoinJavaComponent.inject(RetrieveUserTimetableUseCase::class.java)
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val email = intent?.getStringExtra("email")
//            val partOfTheDay = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
//                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 -> 0
//                12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 -> 1
//                else -> throw IllegalArgumentException("Wrong hour")
//            }
//            // [END]
//            when (val result =
//                withContext(Dispatchers.IO) {
//                    retrieveTimetableUseCase.invoke(
//                        "http://daotao.sict.udn.vn/tkb",
//                        email!!
//                    )
//                }) {
//                is Resource.Error -> Timber.d(result.message)
//                is Resource.Success -> {
//                    val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
//                    val list = result.data!!.filter { subject ->
//                        when (subject.dayOfWeek) {
//                            "Hai" -> Calendar.MONDAY == dayOfWeek
//                            "Ba" -> Calendar.TUESDAY == dayOfWeek
//                            "Tư" -> Calendar.WEDNESDAY == dayOfWeek
//                            "Năm" -> Calendar.THURSDAY == dayOfWeek
//                            "Sáu" -> Calendar.FRIDAY == dayOfWeek
//                            "Bảy" -> Calendar.SATURDAY == dayOfWeek
//                            else -> Calendar.SUNDAY == dayOfWeek
//                        }
//                    }
//                    if (list.isEmpty()) {
//                        notificationManager.sendSchoolReminderNotification(
//                            getString(R.string.title_notification_school_reminder_no_subject),
//                            getString(R.string.content_notification_school_reminder_no_subject),
//                            this@SchoolReminderService
//                        )
//                    } else {
//                        when (partOfTheDay) {
//                            0 -> {
//                                val morningSubjects = list.filter { subject ->
//                                    subject.week.trim()
//                                        .isNotEmpty() && subject.lesson.trim()[0].toString()
//                                        .matches(Regex("[1-5]"))
//                                }
//                                if (morningSubjects.isEmpty()) {
//                                    notificationManager.sendSchoolReminderNotification(
//                                        getString(R.string.title_notification_school_reminder_no_subject_morning),
//                                        getString(R.string.content_notification_school_reminder_no_subject_morning),
//                                        this@SchoolReminderService
//                                    )
//                                } else {
//                                    notificationManager.sendSchoolReminderNotification(
//                                        "cc",
//                                        "cc",
//                                        this@SchoolReminderService
//                                    )
//                                }
//                            }
//                            1 -> {
//                                val afternoonSubjects = list.filter { subject ->
//                                    subject.week.trim()
//                                        .isNotEmpty() && subject.lesson.trim()[0].toString()
//                                        .matches(Regex("[6-9]"))
//                                }
//                                if (afternoonSubjects.isEmpty()) {
//                                    notificationManager.sendSchoolReminderNotification(
//                                        getString(R.string.title_notification_school_reminder_no_subject_afternoon),
//                                        getString(R.string.content_notification_school_reminder_no_subject_afternoon),
//                                        this@SchoolReminderService
//                                    )
//                                } else {
//                                    notificationManager.sendSchoolReminderNotification(
//                                        "cc",
//                                        "cc",
//                                        this@SchoolReminderService
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return START_STICKY
//    }
//
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        Timber.i("Service removed!")
//
//        val intent = Intent(this, SchoolReminderService::class.java)
//        intent.`package` = packageName
//
//        val pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        alarmManager.set
//
//        super.onTaskRemoved(rootIntent)
//    }
//}
