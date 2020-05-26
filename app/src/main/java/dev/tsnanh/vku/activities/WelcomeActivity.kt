/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityWelcomeBinding
import dev.tsnanh.vku.network.VKUServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

// Sign In request code
const val RC_SIGN_IN = 100

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide status bar and toolbar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)


        binding.progressBar.animate().alpha(1F).setDuration(200).start()
        if (user != null) {
            user.getIdToken(true).addOnSuccessListener {
                it.token?.let { token ->
                    lifecycleScope.launch {
                        val isRegistered = withContext(Dispatchers.IO) {
                            try {
                                VKUServiceApi.network.isUserRegistered("Bearer $token")
                            } catch (e: Exception) {
                                false
                            }
                        }
                        if (isRegistered) {
                            start()
                        } else {
                            FirebaseAuth.getInstance().signOut()
                            showErrorDialog()
                        }
                    }
                }
            }
        } else {
            // login using Firebase UI
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                        listOf(
                            AuthUI.IdpConfig.GoogleBuilder().build(),
                            AuthUI.IdpConfig.MicrosoftBuilder().build(),
                            AuthUI.IdpConfig.AppleBuilder().build()
                        )
                    )
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    // register user on server
    private suspend fun registerUser(idToken: String) {
        val status = withContext(Dispatchers.IO) {
            VKUServiceApi.network.registerNewUser("Bearer $idToken")
        }
        if (status == "success") {
            start()
        } else {
            showErrorDialog()
        }
    }

    private fun showErrorDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cannot sign in")
            .setMessage("Something went wrong! Try again.")
            .setPositiveButton("OK") { d, _ ->
                d.dismiss()
                this.finish()
            }
            .create()
            .show()
    }

    // start MainActivity
    private fun start() {
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        this@WelcomeActivity.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    user.getIdToken(true).addOnSuccessListener {
                        lifecycleScope.launch {
                            it.token?.let { it1 -> registerUser(it1) }
                        }
                    }
                } else {
                    showErrorDialog()
                }
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
