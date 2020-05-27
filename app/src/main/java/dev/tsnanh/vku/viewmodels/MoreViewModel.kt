/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

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

    private val _signOut = MutableLiveData<Boolean>()
    val signOut: LiveData<Boolean>
        get() = _signOut

    fun onNavigateToSettings() {
        _navigateToSettings.value = true
    }

    fun onNavigatedToSettings() {
        _navigateToSettings.value = null
    }

    fun onSignOut() {
        _signOut.value = true
    }

    fun onSignedOut() {
        _signOut.value = null
    }
}