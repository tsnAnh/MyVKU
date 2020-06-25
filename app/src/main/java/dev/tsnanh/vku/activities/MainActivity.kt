/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainBinding
import dev.tsnanh.vku.utils.Constants
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences

    private val manager by inject(NotificationManager::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DataBinding initialization
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        // Navigation Component things
        navController = findNavController(R.id.fragment)
        navController.addOnDestinationChangedListener(this)
        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnNavigationItemSelectedListener(this)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.registerOnSharedPreferenceChangeListener(this)

        // create notification channel
        createNotificationChannel(
            getString(R.string.new_thread_channel_id), getString(R.string.new_thread_channel_name)
        )
        createNotificationChannel(
            getString(R.string.school_reminder_channel_id),
            getString(R.string.school_reminder_channel_name)
        )
        createNotificationChannel(
            getString(R.string.firebase_forum_notification_channel),
            "Forum Notification"
        )

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d(task.result?.token)
            }
        }
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
                description = getString(R.string.msg_thread_creating)
                enableLights(false)
                enableVibration(true)
            }

            manager.createNotificationChannel(notificationChannel)
        }
    }

    // Bottom Navigation View callback
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_forum -> navController.navigate(
                R.id.navigation_forum
            )
            R.id.navigation_news -> navController.navigate(
                R.id.navigation_news
            )
            R.id.navigation_more -> navController.navigate(
                R.id.navigation_more
            )
            R.id.navigation_notifications -> navController.navigate(
                R.id.navigation_notifications
            )
            R.id.navigation_timetable -> navController.navigate(
                R.id.navigation_timetable
            )
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.navigation_new_thread -> {
                toggleBottomNav(true)
            }
            R.id.navigation_forum -> {
                toggleBottomNav(true)
            }
            R.id.navigation_replies -> {
                toggleBottomNav(false)
            }
            R.id.navigation_image_viewer -> toggleBottomNav(false)
            else -> {
                toggleBottomNav(true)
            }
        }
    }

    /**
     * Call from fragment to hide bottom nav
     */
    private fun toggleBottomNav(visible: Boolean) {
        binding.bottomNavView.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?,
        key: String?
    ) {
        key?.let {
            if (key == getString(R.string.night_mode_key)) {
                val mode = sharedPreferences?.getString(key, Constants.MODE_SYSTEM)

                AppCompatDelegate.setDefaultNightMode(
                    when (mode) {
                        Constants.MODE_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                        Constants.MODE_DARK -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                )
            }
        }
    }
}