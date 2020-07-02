/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainBinding
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.createNotificationChannel
import timber.log.Timber
import kotlin.math.hypot

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        // DataBinding initialization
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        // Navigation Component things
        navController = findNavController(R.id.fragment)
        navController.addOnDestinationChangedListener(this)
        binding.bottomNavView.apply {
            setupWithNavController(navController)
            setOnNavigationItemSelectedListener(this@MainActivity)
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this).also {
            it.registerOnSharedPreferenceChangeListener(this)
        }

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
            R.id.navigation_replies, R.id.navigation_image_viewer, R.id.navigation_new_reply ->
                toggleBottomNav(false)
            else -> toggleBottomNav(true)
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

    private fun setTheme(mode: String) {
        if (binding.imageOverlay.isVisible) {
            return
        }

        val w = binding.container.measuredWidth
        Timber.i("$w")
        val h = binding.container.measuredHeight

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        binding.container.draw(canvas)

        binding.imageOverlay.setImageBitmap(bitmap)
        // Glide.with(binding.imageOverlay).load(R.drawable.vku_logo_official).into(binding.imageOverlay)
        binding.imageOverlay.isVisible = true

        val finalRadius = hypot(w.toFloat(), h.toFloat())

        sharedPreferences.edit {
            putString(getString(R.string.night_mode_key), mode)
        }

        val anim = ViewAnimationUtils.createCircularReveal(
            binding.imageOverlay,
            w / 2,
            h / 2,
            0f,
            finalRadius
        )
        anim.duration = 400L
        anim.doOnEnd {
            binding.imageOverlay.setImageDrawable(null)
            binding.imageOverlay.isVisible = false
        }
        anim.start()
    }
}