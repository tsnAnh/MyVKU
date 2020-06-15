/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityWelcomeBinding
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.setSchoolReminderAlarm
import dev.tsnanh.vku.viewmodels.my_vku.WelcomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel: WelcomeViewModel by viewModels()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide status bar and toolbar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        binding.lifecycleOwner = this
        binding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)

        val gso = GoogleSignInOptions.Builder()
            .requestId()
            .requestIdToken(getString(R.string.client_id))
            .requestProfile()
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        binding.googleSignInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()
        showProgressBar()
        mGoogleSignInClient.silentSignIn().addOnCompleteListener {
            if (it.isSuccessful) {
                try {
                    val account = it.getResult(ApiException::class.java)!!
                    updateUI(account)
                    Timber.d("Token: ${account.idToken}")
                } catch (e: ApiException) {
                    updateUI(null)
                }
            } else {
                hideProgressBar()
            }
        }
    }
    // region old updateUI function
/*
    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            showProgressBar()
            account.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    try {
                        val token = it.getResult(ApiException::class.java)!!.token!!
                        lifecycleScope.launch(Dispatchers.IO) {
                            val response = async {
                                viewModel.hasUser(token)
                            }
                            if (response.await().user.id.isNotEmpty()) {
                                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                            }
                        }
                    } catch (e: ApiException) {
                        throw e
                    }
                } else {
                    showProgressBar()
                }
            }
        } else {
            hideProgressBar()
        }
    }*/

    // endregion

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            showProgressBar()
            setSchoolReminderAlarm(account.email!!)
            account.idToken!!.let {
                lifecycleScope.launch {
                    val response =
                        withContext(Dispatchers.IO) {
                            viewModel.hasUser(it)
                        }
                    when (response) {
                        is Resource.Success -> {
                            if (response.data!!.user.id.isNotEmpty()) {
                                val sharedPreferences =
                                    PreferenceManager.getDefaultSharedPreferences(this@WelcomeActivity)
                                if (sharedPreferences.getBoolean(
                                        getString(R.string.elearning_mode),
                                        false
                                    )
                                ) {
                                    startActivity(
                                        Intent(
                                            this@WelcomeActivity,
                                            ElearningMainActivity::class.java
                                        )
                                    )
                                } else {
                                    startActivity(
                                        Intent(
                                            this@WelcomeActivity,
                                            MainActivity::class.java
                                        )
                                    )
                                }
                                finish()
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            Snackbar
                                .make(binding.root, response.message!!, Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.text_exit)) {
                                    this@WelcomeActivity.finish()
                                }
                                .show()
                        }
                    }
                }
            }
        } else {
            hideProgressBar()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            // handle sign in result
            try {
                val account = task.getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!)
                updateUI(account)
            } catch (e: ApiException) {
                updateUI(null)
            }
        }
    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        showProgressBar()
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    updateUI(user)
//                }
//            }
//    }

    private fun showProgressBar() {
        binding.googleSignInButton.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.googleSignInButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }
}
