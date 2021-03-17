package dev.tsnanh.myvku.custom

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.appbar.MaterialToolbar
import dev.tsnanh.myvku.databinding.VkuTopAppBarBinding
import dev.tsnanh.myvku.extension.load
import dev.tsnanh.myvku.views.settings.SettingsActivity

class VKUTopAppBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialToolbar(context, attributeSet, defStyleAttr) {
    private val binding = VkuTopAppBarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            binding.avatar.load(account.photoUrl!!, circleCrop = true)
        } else {
            // Load account placeholder
        }
        binding.toolbar.setNavigationOnClickListener {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }
}