package dev.tsnanh.vku.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainBinding
import dev.tsnanh.vku.domain.ForumThread
import dev.tsnanh.vku.utils.sendNotification
import org.koin.java.KoinJavaComponent.inject

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences
    private val viewModel: MainViewModel by viewModels()

    private val manager by inject(NotificationManager::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        navController = findNavController(R.id.fragment)
        navController.addOnDestinationChangedListener(this)
        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnNavigationItemSelectedListener(this)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.registerOnSharedPreferenceChangeListener(this)

        // create notification channel
        createChannelNewThread(
            getString(R.string.new_thread_channel_id), getString(R.string.new_thread_channel_name)
        )

        viewModel.createThreadWorkerLiveData.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                return@Observer
            }
            val workInfo = it[0]

            if (workInfo.state.isFinished) {
                val jsonAdapter =
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        .adapter(ForumThread::class.java)
                val json = workInfo.outputData.getString("thread")!!
                val thread = jsonAdapter.fromJson(json)
                thread?.let {
                    manager.sendNotification(
                        thread.title,
                        "${thread.title} is successfully created!",
                        this
                    )
                }
                WorkManager.getInstance(this).pruneWork()
            }
        })
    }

    private fun createChannelNewThread(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
                description = "Your thread is being created..."
                enableLights(false)
                enableVibration(true)
            }

            manager.createNotificationChannel(notificationChannel)
        }
    }

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
            R.id.navigation_relax -> navController.navigate(
                R.id.navigation_relax
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
            else -> {
                toggleBottomNav(true)
            }
        }
    }

    private fun toggleBottomNav(visible: Boolean) {
        binding.bottomNavView.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let {
            if (key == getString(R.string.night_mode_key)) {
                val mode = sharedPreferences?.getString(key, "system")

                AppCompatDelegate.setDefaultNightMode(
                    when (mode) {
                        "light" -> AppCompatDelegate.MODE_NIGHT_NO
                        "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                )
            }
        }
    }
}