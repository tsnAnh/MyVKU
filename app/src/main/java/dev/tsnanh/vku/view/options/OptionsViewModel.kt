package dev.tsnanh.vku.view.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class OptionsViewModel : ViewModel() {
    private val _user = MutableLiveData<FirebaseUser>(FirebaseAuth.getInstance().currentUser)
    val user: LiveData<FirebaseUser>
        get() = _user
}