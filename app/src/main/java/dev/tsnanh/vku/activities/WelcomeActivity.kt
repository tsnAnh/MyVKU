/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityWelcomeBinding
import dev.tsnanh.vku.utils.ConnectivityLiveData
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.showSnackbar
import dev.tsnanh.vku.viewmodels.LoginState.*
import dev.tsnanh.vku.viewmodels.WelcomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel: WelcomeViewModel by viewModels()

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var isNetworkAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide status bar and toolbar
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        binding.appVersion.text = try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            getString(R.string.text_unknown_version)
        }

        binding.lifecycleOwner = this
        binding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)

        binding.googleSignInButton.setOnClickListener {
            if (isNetworkAvailable) {
                signIn()
            } else {
                showSnackbar(binding.root,
                    getString(R.string.text_no_internet_connection))
            }
        }

        ConnectivityLiveData(getSystemService()).observe(this) { available ->
            isNetworkAvailable = available
        }

        viewModel.error.observe(this) { error ->
            binding.progressBar.isVisible = false
            binding.googleSignInButton.isVisible = true

            mGoogleSignInClient.signOut()

            when (error) {
                is HttpException -> {
                    when (error.code()) {
                        403 -> showSnackbar(
                            binding.root, getString(R.string.text_require_vku_email)
                        )
                    }
                }
                is ConnectException -> showSnackbar(
                    binding.root,
                    getString(R.string.text_no_internet_connection)
                )
            }
        }

        viewModel.loginResponse.observe(this) { response ->
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java).apply {
                putExtra("isNew", response.isNew)
            }).also { this@WelcomeActivity.finish() }
        }

        viewModel.loginState.observe(this) { state ->
            state?.let {
                when (state) {
                    AUTHENTICATING -> {
                        binding.progressBar.isVisible = true
                        binding.googleSignInButton.isVisible = false
                    }
                    UNAUTHENTICATED -> {
                        binding.progressBar.isVisible = false
                        binding.googleSignInButton.isVisible = true
                    }
                    AUTHENTICATED -> {
                        // do sth
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            this@WelcomeActivity.finish()
        }
    }

    private fun signIn() {
        startActivityForResult(mGoogleSignInClient.signInIntent, Constants.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            try {
                viewModel.login()
            } catch (e: Exception) {
                // TODO: 04/09/2020 handle exceptions
                Timber.e(e)
            }
        }
    }
}
