/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoreViewModel : ViewModel() {

    private val _navigateToSettings = MutableLiveData<Boolean>()
    val navigateToSettings: LiveData<Boolean>
        get() = _navigateToSettings

    private val _signOut = MutableLiveData<Boolean>()
    val signOut: LiveData<Boolean>
        get() = _signOut

    private val _navigateToTeacherEvaluation = MutableLiveData<Boolean>()
    val navigateToTeacherEvaluation: LiveData<Boolean>
        get() = _navigateToTeacherEvaluation

    private val _navigateToElearning = MutableLiveData<Boolean>()
    val navigateToElearning: LiveData<Boolean>
        get() = _navigateToElearning

    fun onNavigateToElearning() {
        _navigateToElearning.value = true
    }

    fun onNavigatedToElearning() {
        _navigateToElearning.value = null
    }

    fun onNavigateToTeacherEvaluation() {
        _navigateToTeacherEvaluation.value = true
    }

    fun onNavigatedToTeacherEvaluation() {
        _navigateToTeacherEvaluation.value = null
    }

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