/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainBinding
import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.*
import dev.tsnanh.vku.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.hypot

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavController.OnDestinationChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    @Inject lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel: MainViewModel by viewModels()

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
            setOnNavigationItemReselectedListener { /* Prevent fragment recreation */ }
        }

        sharedPreferences.also {
            it.registerOnSharedPreferenceChangeListener(this)
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
            "Forum Notification"
        )

        // silent Sign In
        mGoogleSignInClient.silentSignIn().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.updateAccount(Resource.Success(account))
                } catch (e: ApiException) {
                    showSnackbarWithAction(
                        binding.root,
                        "Something went wrong with your Google Account. Please log in again!",
                        "Log out", {
                            mGoogleSignInClient.signOut().addOnCompleteListener {
                                startActivity(Intent(this, WelcomeActivity::class.java))
                                finish()
                            }
                        }, binding.bottomNavView
                    )
                    viewModel.updateAccount(Resource.Error("googleException"))
                } catch (ex: Exception) {
                    viewModel.updateAccount(Resource.Error("exception"))
                    Timber.e(ex)
                }
            }
        }

        viewModel.accountStatus.observe<Resource<GoogleSignInAccount>>(this) { result ->
            when (result) {
                is Resource.Error -> {
                    showSnackbarWithAction(
                        binding.root,
                        "Error",
                        null,
                        null,
                        binding.bottomNavView
                    )
                }
                is Resource.Success -> {
                    result.data?.email?.let { setSchoolReminderAlarm(it) }
                    if (isInternetAvailable(this)) {
                        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                            if (task.isComplete) {
                                lifecycleScope.launch {
                                    val response =
                                        withContext(Dispatchers.IO) {
                                            Timber.d(task.result?.token!!)
                                            viewModel.login(
                                                result.data?.idToken!!,
                                                LoginBody(task.result!!.token)
                                            )
                                        }
                                    when (response) {
                                        is Resource.Success -> {
                                            if (response.data!!.user.id.isNotEmpty()) {
                                                showSnackbarWithAction(
                                                    binding.root,
                                                    "Logged in as ${response.data?.user?.email}",
                                                    null, null,
                                                    binding.bottomNavView
                                                )
                                            }
                                        }
                                        is Resource.Error -> {
                                            if (response.message == "403 Forbidden") {
                                                mGoogleSignInClient.signOut()
                                                Snackbar
                                                    .make(
                                                        binding.root,
                                                        "Bạn phải sử dụng email \"sict.udn.vn\" để có " +
                                                                "thể đăng nhập vào ứng dụng!",
                                                        Snackbar.LENGTH_LONG
                                                    )
                                                    .show()
                                            } else if (response.message == "") {

                                            } else
                                                Snackbar
                                                    .make(
                                                        binding.root,
                                                        response.message!!,
                                                        Snackbar.LENGTH_INDEFINITE
                                                    )
                                                    .setAction(getString(R.string.text_exit)) {
                                                        this@MainActivity.finish()
                                                    }
                                                    .show()
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        showSnackbarWithAction(
                            binding.root,
                            "Offline mode",
                            null,
                            null,
                            binding.bottomNavView
                        )
                    }
                }
                is Resource.Loading -> TODO("implement later")
            }
        }
    }

    // Bottom Navigation View callback
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return navigateTo(item.itemId)
    }

    private fun navigateTo(itemId: Int): Boolean {
        return if (navController.currentDestination?.id != itemId) {
            navController.navigate(itemId)
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
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.navigation_replies,
            R.id.navigation_image_viewer,
            R.id.navigation_new_reply,
            R.id.navigation_new_thread -> toggleBottomNav(false)
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