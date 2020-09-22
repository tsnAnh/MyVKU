/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityMainBinding
import dev.tsnanh.vku.services.NEWS_NOTIFY_CHANNEL_ID
import dev.tsnanh.vku.utils.*
import dev.tsnanh.vku.viewmodels.LoginState
import dev.tsnanh.vku.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
    DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel: MainViewModel by viewModels()

    private var isNetworkAvailable = false
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var clickedNavItem = R.id.navigation_news
    private var confirmNavigate = R.id.navigation_news

    private val listSpecialNavigation = listOf(
        R.id.navigation_update_reply,
        R.id.navigation_image_viewer,
        R.id.navigation_replies,
        R.id.navigation_thread,
        R.id.navigation_new_reply,
        R.id.navigation_new_thread
    )

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

        setSupportActionBar(binding.toolbar)

        // Navigation Component things
        navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.toolbar.title = null

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

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
        notificationManager?.createNotificationChannel(
            NEWS_NOTIFY_CHANNEL_ID,
            getString(R.string.text_news)
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_new_thread, R.id.navigation_image_viewer, R.id.navigation_new_reply, R.id.navigation_update_reply, R.id.navigation_thread, R.id.navigation_replies -> {
                    binding.appBarLayout.isVisible = false
                }
                else -> binding.appBarLayout.isVisible = true
            }
            binding.navView.setCheckedItem(destination.id)
            drawerToggleDelegate?.setActionBarUpIndicator(ContextCompat.getDrawable(this@MainActivity,
                R.drawable.ic_baseline_sort_24),
                androidx.navigation.ui.R.string.nav_app_bar_open_drawer_description)
            binding.toolbar.title = null
        }

        viewModel.loginState.observe(this) { state ->
            state?.let {
                when (state) {
                    LoginState.AUTHENTICATING -> {

                    }
                    LoginState.UNAUTHENTICATED -> {

                    }
                    LoginState.AUTHENTICATED -> {
                        val email = GoogleSignIn.getLastSignedInAccount(this)?.email
                        if (!email.isNullOrBlank()) {
                            setSchoolReminderAlarm(email)
                            showSnackbar(
                                binding.root,
                                getString(R.string.text_logged_in_as, email),
                                null,
                                null,
                            )
                        }
                    }
                }
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                when (error) {
                    is HttpException -> {
                        when (error.code()) {
                            403 -> showSnackbar(
                                binding.root,
                                getString(R.string.text_require_vku_email),
                                null,
                                null,
                            )
                        }
                    }
                    is ConnectException -> showSnackbar(
                        binding.root,
                        getString(R.string.text_cannot_connect_to_server),
                        null, null,
                    )
                    is SocketTimeoutException -> {
                        // TODO: handle socket timeout
                    }
                    else -> Timber.e(error)
                }
            }
        }

        viewModel.connectivityLiveData.observe(this) { available ->
            isNetworkAvailable = available
        }

        Glide
            .with(this)
            .load(GoogleSignIn.getLastSignedInAccount(this)?.photoUrl)
            .circleCrop()
            .into(binding.avatar)

        binding.drawerLayout.addDrawerListener(this)

        binding.navView.setNavigationItemSelectedListener(this)

        viewModel.clickedItemId?.observe(this) { value ->
            clickedNavItem = value
        }

        viewModel.confirmNavigate?.observe(this) { value ->
            confirmNavigate = value
        }

        viewModel.signInAgain.observe(this) {
            it?.let {
                startActivityForResult(mGoogleSignInClient.signInIntent, Constants.RC_SIGN_IN)
                viewModel.onSignInAgainComplete()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.silentSignIn()
            } else {
                mGoogleSignInClient.signOut()
                startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()

        // Silent Sign In
        if (isNetworkAvailable || isInternetAvailableApi23()) viewModel.silentSignIn()
    }

    override fun onSupportNavigateUp(): Boolean {
        binding.drawerLayout.openDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_sign_out -> {
                val builder = MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(getString(R.string.text_sign_out))
                    .setMessage(getString(R.string.text_are_u_sure))
                    .setPositiveButton(getString(R.string.text_ok)) { dialog, _ ->
                        mGoogleSignInClient.signOut().addOnCompleteListener { task ->
                            if (task.isComplete) {
                                dialog.dismiss()
                                startActivity(Intent(this@MainActivity,
                                    WelcomeActivity::class.java))
                                finish()
                            }
                        }
                    }
                    .setNegativeButton(getString(R.string.text_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }

                builder.create().show()
                return true
            }
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
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

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        if (navController.currentDestination?.id == R.id.navigation_news) {
            finish()
            return
        }
        if (navController.currentDestination?.id !in listSpecialNavigation) {
            navController.popBackStack(R.id.navigation_news, false)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("confirmNavigate", confirmNavigate)
        outState.putInt("clickedNavItem", clickedNavItem)
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        // Ignore
    }

    override fun onDrawerOpened(drawerView: View) {
        // Ignore
    }

    override fun onDrawerClosed(drawerView: View) {
        if (confirmNavigate != clickedNavItem) {
            confirmNavigate = clickedNavItem
            when (clickedNavItem) {
                R.id.navigation_news -> navController.navigate(R.id.navigation_news)
                R.id.navigation_forum -> navController.navigate(R.id.navigation_forum)
                R.id.navigation_timetable -> navController.navigate(R.id.navigation_timetable)
                R.id.navigation_teacher_evaluation -> navController.navigate(R.id.navigation_teacher_evaluation)
                R.id.navigation_notifications -> navController.navigate(R.id.navigation_notifications)
                R.id.navigation_attendance -> navController.navigate(R.id.navigation_attendance)
            }
        }
    }

    override fun onDrawerStateChanged(newState: Int) {
        // Ignore
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_news -> {
                clickedNavItem = R.id.navigation_news
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.navigation_forum -> {
                clickedNavItem = R.id.navigation_forum
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.navigation_timetable -> {
                clickedNavItem = R.id.navigation_timetable
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.navigation_notifications -> {
                clickedNavItem = R.id.navigation_notifications
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.navigation_teacher_evaluation -> {
                clickedNavItem = R.id.navigation_teacher_evaluation
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.navigation_attendance -> {
                clickedNavItem = R.id.navigation_attendance
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.navigation_elearning -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                val launchIntent =
                    packageManager.getLaunchIntentForPackage("vn.udn.vku.elearning")
                launchIntent?.let { intent ->
                    startActivity(intent)
                }
            }
            R.id.navigation_settings -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(this@MainActivity,
                    SettingsActivity::class.java))
            }
            R.id.navigation_sign_out -> {
                clickedNavItem = R.id.navigation_sign_out
                val builder = MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(getString(R.string.text_sign_out))
                    .setMessage(getString(R.string.text_are_u_sure))
                    .setPositiveButton(getString(R.string.text_ok)) { dialog, _ ->
                        mGoogleSignInClient.signOut().addOnCompleteListener { task ->
                            if (task.isComplete) {
                                dialog.dismiss()
                                startActivity(Intent(this@MainActivity,
                                    WelcomeActivity::class.java))
                                finish()
                            }
                        }
                    }
                    .setNegativeButton(getString(R.string.text_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }

                builder.create().show()
            }
        }
        return true
    }
}