package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PageNewsViewModel @ViewModelInject constructor(
    private val retrieveNewsUseCase: RetrieveNewsUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val news = retrieveNewsUseCase.execute().asLiveData()

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            retrieveNewsUseCase.refresh()
        }
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
}