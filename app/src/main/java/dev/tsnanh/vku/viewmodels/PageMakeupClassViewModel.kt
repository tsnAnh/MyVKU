package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.tsnanh.vku.domain.usecases.RetrieveNoticeUseCase

class PageMakeupClassViewModel @ViewModelInject constructor(
    retrieveNoticeUseCase: RetrieveNoticeUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val loadTrigger = MutableLiveData(Unit)

    fun refresh() {
        loadTrigger.value = Unit
    }

    private val _error = MutableLiveData<Throwable?>()
    val error: LiveData<Throwable?>
        get() = _error

    fun onError(t: Throwable?) {
        _error.value = t
    }

    fun clearError() {
        _error.value = null
    }

    val makeUpClass = loadTrigger.switchMap { retrieveNoticeUseCase.makeUpClass("") }
}