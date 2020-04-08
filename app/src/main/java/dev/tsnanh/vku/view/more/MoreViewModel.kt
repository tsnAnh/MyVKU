package dev.tsnanh.vku.view.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MoreViewModel : ViewModel() {
    private val _user = MutableLiveData<FirebaseUser>(FirebaseAuth.getInstance().currentUser)
    val user: LiveData<FirebaseUser>
        get() = _user

    private val _navigateToSettings = MutableLiveData<Boolean>()
    val navigateToSettings: LiveData<Boolean>
        get() = _navigateToSettings

    fun onNavigateToSettings() {
        _navigateToSettings.value = true
    }

    fun onNavigatedToSettings() {
        _navigateToSettings.value = null
    }
}