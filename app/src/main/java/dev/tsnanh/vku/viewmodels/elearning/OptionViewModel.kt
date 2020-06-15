package dev.tsnanh.vku.viewmodels.elearning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OptionViewModel : ViewModel() {
    private val _backToMyVKU = MutableLiveData<Boolean>()
    val backToMyVKU: LiveData<Boolean>
        get() = _backToMyVKU

    fun navigateBackToMyVKU() {
        _backToMyVKU.value = true
    }

    fun navigatedBackToMyVKU() {
        _backToMyVKU.value = null
    }
}