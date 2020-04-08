package dev.tsnanh.vku.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ActivityWelcomeBinding
import timber.log.Timber

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
const val RC_SIGN_IN = 100

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val user = FirebaseAuth.getInstance().currentUser
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (user == null) {
                    startActivityForResult(
                        AuthUI
                            .getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                listOf(
                                    AuthUI.IdpConfig.GoogleBuilder().build(),
                                    AuthUI.IdpConfig.AppleBuilder().build(),
                                    AuthUI.IdpConfig.MicrosoftBuilder().build()
                                )
                            )
                            .setLogo(R.drawable.vku_logo)
                            .build(),
                        RC_SIGN_IN
                    )
                } else {
                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    this@WelcomeActivity.finish()
                }
            }
        })
    }

    override fun onRestart() {
        binding.motionLayout.transitionToEnd()
        super.onRestart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                sharedPreferences.edit().putString("id_token", response?.idpToken).apply()
                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(intent)
                this@WelcomeActivity.finish()
            } else {
                if (response == null) {
                    Toast
                        .makeText(
                            this@WelcomeActivity,
                            "Sign in canceled!",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                        Toast
                            .makeText(
                                this@WelcomeActivity,
                                "No internet connection!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        return
                    }
                    Toast
                        .makeText(
                            this@WelcomeActivity,
                            "Unknown Error!",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    Timber.e("Sign in Error: ${response.error}")
                }
            }
        }
    }
}
