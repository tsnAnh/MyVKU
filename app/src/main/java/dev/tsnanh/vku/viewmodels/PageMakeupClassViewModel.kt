package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.usecases.RetrieveNoticeUseCase

class PageMakeupClassViewModel @ViewModelInject constructor(
    retrieveNoticeUseCase: RetrieveNoticeUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val makeUpClass = retrieveNoticeUseCase.makeUpClass("")
}