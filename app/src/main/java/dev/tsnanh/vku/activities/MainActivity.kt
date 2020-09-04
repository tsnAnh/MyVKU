/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.app.NotificationManager
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainBinding
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.createNotificationChannel
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.LoginState
import dev.tsnanh.vku.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject
import kotlin.math.hypot

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel: MainViewModel by viewModels()

    private var isNetworkAvailable = false

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

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerNetworkInfoCallback()
        }

        // create notification channel
        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(
            getString(R.string.new_thread_channel_id), getString(R.string.new_thread_channel_name)
        )
        notificationManager?.createNotificationChannel(
            getString(R.string.school_reminder_channel_id),
            getString(R.string.school_reminder_channel_name)
        )
        notificationManager?.createNotificationChannel(
            getString(R.string.firebase_forum_notification_channel),
            getString(R.string.text_forum_notification)
        )

        viewModel.loginState.observe(this) { state ->
            state?.let {
                when (state) {
                    LoginState.AUTHENTICATING -> {

                    }
                    LoginState.UNAUTHENTICATED -> {

                    }
                    LoginState.AUTHENTICATED -> {
                        showSnackbarWithAction(
                            binding.layout,
                            getString(R.string.text_logged_in_as,
                                GoogleSignIn.getLastSignedInAccount(this)?.email),
                            null,
                            null,
                            binding.bottomNavView
                        )
                    }
                }
            }
        }

        viewModel.error.observe(this) { error ->
            when (error) {
                is HttpException -> {
                    when (error.code()) {
                        403 -> showSnackbarWithAction(
                            binding.root, getString(R.string.text_require_vku_email), null, null,
                            binding.bottomNavView
                        )
                    }
                }
                is ConnectException -> showSnackbarWithAction(
                    binding.root,
                    getString(R.string.text_cannot_connect_to_server),
                    null, null,
                    binding.bottomNavView
                )
            }
        }

        viewModel.connectivityLiveData.observe(this) { available ->
            if (available) {
                GoogleSignIn.getLastSignedInAccount(this@MainActivity)?.email?.let {
                    viewModel.refreshData(it)
                }
            } else {
                showSnackbarWithAction(binding.root,
                    getString(R.string.text_no_internet_and_switch_to_offline))
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Silent Sign In
        if (isNetworkAvailable) viewModel.silentSignIn()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun registerNetworkInfoCallback() {
        val connectivityManager = getSystemService<ConnectivityManager>()
        connectivityManager?.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isNetworkAvailable = true
            }

            override fun onLost(network: Network) {
                isNetworkAvailable = false
            }
        })
    }

    // Bottom Navigation View callback
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return if (navController.currentDestination?.id != item.itemId) {
            navController.navigate(item.itemId)
            true
        } else {
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.navigation_news,
            R.id.navigation_forum,
            R.id.navigation_timetable,
            R.id.navigation_notifications,
            R.id.navigation_more,
            -> toggleBottomNav(true)
            else -> toggleBottomNav(false)
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
        key: String?,
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