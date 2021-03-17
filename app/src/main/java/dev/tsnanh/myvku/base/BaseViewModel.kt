package dev.tsnanh.myvku.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {
    protected val _error = MutableStateFlow<Exception?>(null)
    val error: StateFlow<Exception?>
        get() = _error
}
