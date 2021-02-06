/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.main

import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseActivity
import dev.tsnanh.myvku.databinding.ActivityMainBinding
import dev.tsnanh.myvku.services.NEWS_NOTIFY_CHANNEL_ID
import dev.tsnanh.myvku.utils.createNotificationChannel
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DataBinding initialization
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        intent?.handleIntent()

        // Navigation Component things
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        // create notification channel
        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.apply {
            createNotificationChannel(
                getString(R.string.new_thread_channel_id),
                getString(R.string.new_thread_channel_name)
            )
            createNotificationChannel(
                getString(R.string.school_reminder_channel_id),
                getString(R.string.school_reminder_channel_name)
            )
            createNotificationChannel(
                getString(R.string.firebase_forum_notification_channel),
                getString(R.string.text_forum_notification)
            )
            createNotificationChannel(
                NEWS_NOTIFY_CHANNEL_ID,
                getString(R.string.text_news)
            )
        }

        observe()
    }

    private fun observe() {
        jobs.add(
            lifecycleScope.launchWhenStarted {
                viewModel.user.collect { account ->
                    if (account != null) {

                    }
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        viewModel.setUser(account)
    }

    private fun Intent.handleIntent() {
        when (action) {
            Intent.ACTION_VIEW -> handleDeepLink(data)
        }
    }

    private fun handleDeepLink(data: Uri?) {
        when (data?.path.also(::println)) {
            "/today" -> {
                Timber.i("Todayyyyyy!")
            }
            "/tomorrow" -> {
                println("Tomorrowwwwwwww!")
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.handleIntent()
    }
}