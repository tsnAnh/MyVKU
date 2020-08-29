package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase

class PageNewsViewModel @ViewModelInject constructor(
    retrieveNewsUseCase: RetrieveNewsUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val news = retrieveNewsUseCase.execute().asLiveData()
}