/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityWelcomeBinding
import dev.tsnanh.vku.viewmodels.WelcomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

// Sign In request code
const val RC_SIGN_IN = 100

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val user = FirebaseAuth.getInstance().currentUser
    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide status bar and toolbar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // implement new sign in flow
        viewModel.authenticationState.observe(this, Observer {
            it?.let { state ->
                when (state) {
                    WelcomeViewModel.AuthenticationState.UNAUTHENTICATED -> {
                        binding.progressBar.visibility = View.GONE
                        binding.signinButton.visibility = View.VISIBLE
                    }
                    WelcomeViewModel.AuthenticationState.AUTHENTICATED -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.signinButton.visibility = View.INVISIBLE

                        val user = FirebaseAuth.getInstance().currentUser
                        lifecycleScope.launch(Dispatchers.IO) {
                            val response = viewModel.hasUser(user!!.uid)
                            Timber.d(response.isNew.toString())
                            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                            this@WelcomeActivity.finish()

                        }
                    }
                }
            }
        })

        viewModel.signIn.observe(this, Observer {
            it?.let {
                startActivityForResult(
                    AuthUI
                        .getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            listOf(
                                AuthUI.IdpConfig.GoogleBuilder().build()
                            )
                        )
                        .build(),
                    RC_SIGN_IN
                )

                viewModel.onSignInClicked()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
            } else {
                if (response == null) {
                    Snackbar
                        .make(
                            binding.root,
                            "Sign in canceled!",
                            Snackbar.LENGTH_LONG
                        )
                        .show()
                } else {
                    if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                        Snackbar
                            .make(
                                binding.root,
                                "No internet connection!",
                                Snackbar.LENGTH_SHORT
                            )
                            .show()
                        return
                    }
                    Snackbar
                        .make(
                            binding.root,
                            "Unknown Error!",
                            Snackbar.LENGTH_SHORT
                        )
                        .show()
                    Timber.e("Sign in Error: ${response.error}")
                }
            }
        }
    }
}
